package com.santukis.viewmodels.hearthstone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santukis.entities.exceptions.NoMoreData
import com.santukis.entities.hearthstone.*
import com.santukis.usecases.UseCase
import com.santukis.viewmodels.entities.*
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_CLASS
import com.santukis.viewmodels.mappers.toUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

class HearthstoneViewModel(
    private val loadMetadata: UseCase<Regionality, Flow<Result<Metadata>>>,
    private val searchCardsUseCase: UseCase<SearchCardsRequest, Flow<Result<List<Card>>>>,
    private val updateCardFavouriteUseCase: UseCase<Card, Result<Card>>
) : ViewModel() {

    private var searchJob: Job? = null

    var cardCollectionState by mutableStateOf(CardCollectionState())
        private set

    var cardDetailState by mutableStateOf(CardDetailState())
        private set

    var cardFilterState by mutableStateOf(CardFilterState())
        private set

    var uiState by mutableStateOf(UiState())
        private set

    private var searchCardsRequest: SearchCardsRequest = SearchCardsRequest(
        regionality = Regionality.Europe(EuropeLocale.Spanish())
    )

    init {
        loadMetadata()
    }

    fun onCardSelected(cardIndex: Int) {
        cardDetailState = cardCollectionState.cards.getOrNull(cardIndex)?.let { selectedCard ->
            loadRelatedCards(selectedCard)
            cardDetailState.copy(
                card = selectedCard,
                cardIndex = cardIndex
            )

        } ?: cardDetailState.reset()
    }

    fun onCardClassSelected(cardClass: CardClass) {
        cardDetailState = cardDetailState.reset()

        cardCollectionState = cardCollectionState.copy(
            cards = emptyList()
        )

        onFilterSelected(CARD_CLASS, cardClass.asCardFilter())
    }

    fun onFilterSelected(key: Int, filter: CardFilter<*>) {
        cardFilterState = cardFilterState.copy(
            activeFilters = cardFilterState.updateActiveFilters(key, filter)
        )

        loadMoreItems()
    }

    fun onRemoveFilterClick(filter: Int) {
        cardFilterState = cardFilterState.copy(
            activeFilters = cardFilterState.updateActiveFilters(filter, null)
        )

        loadMoreItems()
    }

    fun onEndReached() {
        loadMoreItems()
    }

    fun onFavouriteClick() {
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

    private fun loadMoreItems() {
        searchCards(
            cardRequest = buildSearchCardRequest(),
            onSuccess = { cards ->
                cardCollectionState = cardCollectionState.copy(cards = cards.toList())

                cardDetailState = cardDetailState.copy(
                    card = cards.getOrNull(cardDetailState.cardIndex)
                        ?: cards.getOrNull(cards.lastIndex),
                    relatedCards = emptyList()
                )
            },
            onError = {
                if (it !is NoMoreData) {
                    cardCollectionState = cardCollectionState.copy(cards = emptyList())
                    cardDetailState = cardDetailState.reset()
                }
            }
        )
    }

    private fun loadMetadata() {
        viewModelScope.launch(Dispatchers.Main) {
            loadMetadata(Regionality.Europe(EuropeLocale.Spanish()))
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    result.onSuccess { metadata ->
                        cardFilterState = cardFilterState.copy(
                            metadata = metadata,
                            cardFilters = metadata.asCardFilters(),
                            activeFilters = cardFilterState
                                .updateActiveFilters(
                                    key = CARD_CLASS,
                                    filter = metadata.classes.firstOrNull()?.asCardFilter()
                                )
                        )
                    }
                }
        }
    }

    private fun loadRelatedCards(selectedCard: Card) {
        searchCards(
            SearchCardsRequest(
                regionality = Regionality.Europe(EuropeLocale.Spanish()),
                keyword = selectedCard.keywords.takeIf { it.isNotEmpty() }?.random(),
                cardStats = CardStats(manaCost = selectedCard.cardStats.manaCost),
                cardClass = selectedCard.cardClass,
                type = selectedCard.cardType
            ),
            onSuccess = { cards ->
                cardDetailState = cardDetailState.copy(relatedCards = cards.toList())
            }
        )
    }

    private fun searchCards(
        cardRequest: SearchCardsRequest,
        onSuccess: (List<Card>) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
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
                        .onFailure { error -> onError(error) }
                }
        }
    }

    private fun buildSearchCardRequest(): SearchCardsRequest {
        searchCardsRequest = cardFilterState.buildSearchCardsRequest(searchCardsRequest)

        return searchCardsRequest.copy()
    }
}