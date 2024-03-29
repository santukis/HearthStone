package com.santukis.viewmodels.hearthstone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santukis.entities.exceptions.NoMoreData
import com.santukis.entities.hearthstone.*
import com.santukis.entities.paging.PagingRequest
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
    private val searchCardsUseCase: UseCase<PagingRequest<SearchCardsRequest>, Flow<Result<List<Card>>>>,
    private val updateCardFavouriteUseCase: UseCase<Card, Result<Card>>
) : ViewModel() {

    private var relatedCardsJob: Job? = null

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

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is OnCardSelected -> onCardSelected(uiEvent.cardIndex)
            is OnFilterSelected -> updateFilter(uiEvent.key, uiEvent.filter)
            is OnFilterRemoved -> updateFilter(uiEvent.key, null)
            is OnFavouriteClick -> onFavouriteClick()
            is OnEndReached -> loadMoreItems(lastItemPosition = uiEvent.lastItemPosition)
        }
    }

    private fun onCardSelected(cardIndex: Int) {
        cardDetailState = cardCollectionState.cards.getOrNull(cardIndex)?.let { selectedCard ->
            loadRelatedCards(selectedCard)
            cardDetailState.copy(
                card = selectedCard,
                cardIndex = cardIndex
            )

        } ?: cardDetailState.reset()
    }

    private fun updateFilter(key: Int, filter: CardFilter<*>?) {
        cardFilterState = cardFilterState.copy(
            activeFilters = cardFilterState.updateActiveFilters(key, filter)
        )

        cardDetailState = cardDetailState.reset()

        cardCollectionState = cardCollectionState.reset()

        loadMoreItems(shouldRefresh = true)
    }

    private fun onFavouriteClick() {
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

    private fun loadMoreItems(
        shouldRefresh: Boolean = false,
        lastItemPosition: Int = 0
    ) {
        searchCards(
            cardRequest = buildSearchCardRequest(shouldRefresh, lastItemPosition),
            onSuccess = { cards ->
                val updatedCards = cardCollectionState.cards
                    .toMutableList()
                    .apply { addAll(cards) }
                    .distinctBy { it.identity.name }

                cardCollectionState = cardCollectionState.copy(
                    cards = updatedCards
                )

                cardDetailState = cardDetailState.copy(
                    card = cards.getOrNull(cardDetailState.cardIndex)
                        ?: cards.getOrNull(cards.lastIndex),
                    relatedCards = emptyList()
                )
            },
            onError = {
                if (it !is NoMoreData) {
                    cardCollectionState = cardCollectionState.reset()
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
                                    filter = null
                                )
                        )

                        loadMoreItems()
                    }
                }
        }
    }

    private fun loadRelatedCards(selectedCard: Card) {
        relatedCardsJob?.cancel(CancellationException("New Search required"))

        relatedCardsJob = searchCards(
            PagingRequest(
                shouldRefresh = true,
                request = SearchCardsRequest(
                    regionality = Regionality.Europe(EuropeLocale.Spanish()),
                    keyword = selectedCard.keywords.takeIf { it.isNotEmpty() }?.random(),
                    cardStats = CardStats(manaCost = selectedCard.cardStats.manaCost),
                    cardClass = selectedCard.cardClass,
                    type = selectedCard.cardType
                )
            ),
            onSuccess = { cards ->
                cardDetailState = cardDetailState.copy(
                    relatedCards = cards.toList().distinctBy { it.identity.name }
                )
            }
        )
    }

    private fun searchCards(
        cardRequest: PagingRequest<SearchCardsRequest>,
        onSuccess: (List<Card>) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Job =
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun buildSearchCardRequest(
        shouldRefresh: Boolean,
        lastItemPosition: Int
    ): PagingRequest<SearchCardsRequest> {
        searchCardsRequest = cardFilterState.buildSearchCardsRequest(searchCardsRequest)

        return PagingRequest(
            shouldRefresh = shouldRefresh,
            itemCount = lastItemPosition,
            request = searchCardsRequest.copy()
        )
    }
}