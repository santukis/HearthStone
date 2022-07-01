package com.santukis.viewmodels.hearthstone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santukis.entities.hearthstone.*
import com.santukis.usecases.UseCase
import com.santukis.viewmodels.entities.CardCollectionState
import com.santukis.viewmodels.entities.CardDetailState
import com.santukis.viewmodels.entities.UiState
import com.santukis.viewmodels.mappers.toUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

class HearthstoneViewModel(
    private val loadDeckUseCase: UseCase<DeckRequest, Flow<Result<Deck>>>,
    private val searchCardsUseCase: UseCase<SearchCardsRequest, Flow<Result<List<Card>>>>,
    private val updateCardFavouriteUseCase: UseCase<Card, Result<Card>>
) : ViewModel() {

    private var searchJob: Job? = null

    var cardCollectionState by mutableStateOf(CardCollectionState())
        private set

    var cardDetailState by mutableStateOf(CardDetailState())
        private set

    var uiState by mutableStateOf(UiState())
        private set

    fun loadDeck(deckCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadDeckUseCase(
                DeckRequest(
                    regionality = Regionality.Europe(EuropeLocale.Spanish()),
                    deckCode = deckCode
                )
            )
                .onStart {
                    uiState = uiState.copy(isLoading = true)
                }
                .single()
                .onSuccess { deck ->
                    cardCollectionState = cardCollectionState.copy(cards = deck.cards)
                }
                .onFailure { error -> uiState = error.toUiState(uiState) }
        }
    }

    fun onCardSelected(cardIndex: Int) {
        cardDetailState = cardCollectionState.cards.getOrNull(cardIndex)?.let { selectedCard ->
            loadRelatedCards(selectedCard)
            cardDetailState.copy(
                card = selectedCard,
                cardIndex = cardIndex
            )

        } ?: cardDetailState.copy(
            card = null,
            cardIndex = -1,
            relatedCards = listOf()
        )
    }

    fun loadMoreItems(itemCount: Int) {
        searchCards(
            cardRequest = SearchCardsRequest(
                regionality = Regionality.Europe(EuropeLocale.Spanish()),
                spellSchool = SpellSchool(identity = Identity(id = 1, slug = "arcane")),
                itemCount = itemCount
            ),
            onSuccess = { cards ->
                cardCollectionState = cardCollectionState.copy(cards = cards.toList())

                if (itemCount == 0) {
                    cardDetailState = cardDetailState.copy(
                        card = cards.first(),
                        cardIndex = 0
                    )
                }
            }
        )
    }

    fun updateCardFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            cardCollectionState.cards.getOrNull(cardDetailState.cardIndex)?.let { selectedCard ->
                updateCardFavouriteUseCase(selectedCard)
                    .onSuccess { updatedCard ->
                        cardDetailState = cardDetailState.copy(card = updatedCard)
                        cardCollectionState = cardCollectionState.copy(
                            cards = cardCollectionState.cards.toMutableList().apply {
                                set(cardDetailState.cardIndex, updatedCard)
                            }
                        )
                    }
                    .onFailure { error ->
                        uiState = error.toUiState(uiState)
                    }

            }
        }
    }

    private fun loadRelatedCards(selectedCard: Card) {
        searchCards(
            SearchCardsRequest(
                regionality = Regionality.Europe(EuropeLocale.Spanish()),
                keyword = selectedCard.keywords.firstOrNull(),
                cardStats = selectedCard.cardStats,
                cardClass = selectedCard.cardClass,
                type = selectedCard.cardType
            ),
            onSuccess = { cards ->
                cardDetailState = cardDetailState.copy(relatedCards = cards.toList())
            }
        )
    }

    private fun searchCards(cardRequest: SearchCardsRequest, onSuccess: (List<Card>) -> Unit = {}) {
        searchJob?.cancel(CancellationException("New Search required"))

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchCardsUseCase(cardRequest)
                .onStart {
                    uiState = uiState.copy(isLoading = true)
                }
                .catch { error -> uiState = error.toUiState(uiState) }
                .collect { result ->
                    result
                        .onSuccess { cards -> onSuccess(cards) }
                        .onFailure { error -> uiState = error.toUiState(uiState) }
                }
        }
    }
}