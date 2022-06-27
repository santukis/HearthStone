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
import com.santukis.viewmodels.mappers.toErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HearthstoneViewModel(
    private val loadDeck: UseCase<DeckRequest, Flow<Result<Deck>>>,
    private val searchCards: UseCase<SearchCardsRequest, Flow<Result<List<Card>>>>
) : ViewModel() {

    private var searchJob: Job? = null

    var cardCollectionState by mutableStateOf(CardCollectionState())
        private set

    var cardDetailState by mutableStateOf(CardDetailState())
        private set

    fun loadDeck(deckCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadDeck(
                DeckRequest(
                    regionality = Regionality.Europe(EuropeLocale.Spanish()),
                    deckCode = deckCode
                )
            )
                .onStart {
                    cardCollectionState = cardCollectionState.copy(isLoading = true)
                }
                .single()
                .onSuccess { deck ->
                    cardCollectionState = CardCollectionState(cards = deck.cards)
                }
                .onFailure {
                    cardCollectionState = cardCollectionState.copy(
                        isLoading = false,
                        errorMessage = it.toErrorMessage()
                    )
                }
        }
    }

    fun onCardSelected(card: Int) {
        cardDetailState = cardCollectionState.cards.getOrNull(card)?.let { selectedCard ->
            loadRelatedCards(selectedCard)
            cardDetailState.copy(card = selectedCard)

        } ?: cardDetailState.copy(card = null)
    }

    fun searchCards(itemCount: Int) {
        search(
            cardRequest = SearchCardsRequest(
                regionality = Regionality.Europe(EuropeLocale.Spanish()),
                spellSchool = SpellSchool(identity = Identity(id = 1, slug = "arcane")),
                itemCount = itemCount
            ),
            onSuccess = { cards ->
                cardCollectionState = cardCollectionState.copy(isLoading = false, cards = cards.toList())
            }
        )
    }

    private fun loadRelatedCards(selectedCard: Card) {
        search(
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

    private fun search(cardRequest: SearchCardsRequest, onSuccess: (List<Card>) -> Unit = {}) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchCards(cardRequest)
                .onStart {
                    cardCollectionState = cardCollectionState.copy(isLoading = true)
                }
                .catch {
                    cardCollectionState = cardCollectionState.copy(
                        isLoading = false,
                        errorMessage = it.toErrorMessage()
                    )
                }
                .takeWhile { it.isSuccess }
                .collect { result ->
                    result
                        .onSuccess { cards -> onSuccess(cards) }
                        .onFailure {
                            cardCollectionState = cardCollectionState.copy(
                                isLoading = false,
                                errorMessage = it.toErrorMessage()
                            )
                        }
                }
        }
    }
}