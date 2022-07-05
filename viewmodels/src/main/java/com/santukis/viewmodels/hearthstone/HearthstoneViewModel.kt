package com.santukis.viewmodels.hearthstone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santukis.entities.hearthstone.*
import com.santukis.usecases.UseCase
import com.santukis.viewmodels.R
import com.santukis.viewmodels.entities.*
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
        cardFilterState = cardFilterState.copy(
            selectedCardClass = cardClass,
            shouldShowCardClassList = !cardFilterState.shouldShowCardClassList
        )

        cardDetailState = cardDetailState.reset()

        cardCollectionState = cardCollectionState.copy(
            cards = emptyList()
        )

        loadMoreItems()
    }

    fun onSelectedCardClassClick() {
        cardFilterState = cardFilterState.copy(
            shouldShowCardClassList = !cardFilterState.shouldShowCardClassList
        )
    }

    fun onManaCostSelected(cost: Int) {
        cardFilterState = cardFilterState.copy(
            selectedCardStats = cardFilterState.selectedCardStats.copy(manaCost = cost),
            activeFilters = cardFilterState.updateActiveFilters(
                FilterUI(
                    key = R.string.mana_cost_filter,
                    value = cost.toString()
                )
            )
        )

        loadMoreItems()
    }

    fun onCardRaritySelected(rarity: Rarity?) {
        cardFilterState = cardFilterState.copy(
            selectedCardRarity = rarity,
            activeFilters = cardFilterState.updateActiveFilters(
                FilterUI(
                    key = R.string.rarity_filter,
                    value = rarity?.identity?.name ?: CardFilterState.UNSELECTED
                )
            )
        )

        loadMoreItems()
    }

    fun onSpellSchoolSelected(spellSchool: SpellSchool?) {
        cardFilterState = cardFilterState.copy(
            selectedSpellSchool = spellSchool,
            activeFilters = cardFilterState.updateActiveFilters(
                FilterUI(
                    key = R.string.spell_school_filter,
                    value = spellSchool?.identity?.name ?: CardFilterState.UNSELECTED
                )
            )
        )

        loadMoreItems()
    }

    fun onRemoveFilterClick(filter: Int) {
        when (filter) {
            R.string.mana_cost_filter -> onManaCostSelected(-1)
            R.string.rarity_filter -> onCardRaritySelected(null)
            R.string.spell_school_filter -> onSpellSchoolSelected(null)
        }
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
                cardCollectionState = cardCollectionState.copy(cards = emptyList())
                cardDetailState = cardDetailState.reset()
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
                            selectedCardClass = metadata.classes.firstOrNull()
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
        searchCardsRequest = searchCardsRequest.copy(
            cardClass = cardFilterState.selectedCardClass,
            cardStats = cardFilterState.selectedCardStats,
            rarity = cardFilterState.selectedCardRarity,
            spellSchool = cardFilterState.selectedSpellSchool
        )

        return searchCardsRequest.copy()
    }
}