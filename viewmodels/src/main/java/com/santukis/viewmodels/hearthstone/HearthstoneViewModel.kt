package com.santukis.viewmodels.hearthstone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santukis.entities.hearthstone.*
import com.santukis.usecases.UseCase
import com.santukis.viewmodels.entities.CardCollectionState
import com.santukis.viewmodels.mappers.toErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HearthstoneViewModel(
    private val loadDeck: UseCase<DeckRequest, Flow<Result<Deck>>>,
    private val searchCards: UseCase<SearchCardsRequest, Flow<Result<List<Card>>>>
) : ViewModel() {

    var cardCollectionState by mutableStateOf(CardCollectionState())
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
        cardCollectionState = cardCollectionState.copy(selectedCard = cardCollectionState.cards.getOrNull(card))
    }

    fun searchCards(itemCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            searchCards(
                SearchCardsRequest(
                    regionality = Regionality.Europe(EuropeLocale.Spanish()),
                    spellSchool = SpellSchool(identity = Identity(id = 1, slug = "arcane")),
                    itemCount = itemCount
                )
            )
                .onStart {
                    cardCollectionState = cardCollectionState.copy(isLoading = true)
                }
                .catch {
                    cardCollectionState = cardCollectionState.copy(
                        isLoading = false,
                        errorMessage = it.toErrorMessage()
                    )
                }
                .single()
                .onSuccess { cards ->
                    cardCollectionState = CardCollectionState(cards = cards.toList())
                }
                .onFailure {
                    cardCollectionState = cardCollectionState.copy(
                        isLoading = false,
                        errorMessage = it.toErrorMessage()
                    )
                }
        }
    }
}