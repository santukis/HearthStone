package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import com.santukis.entities.core.filterNotNullValues
import com.santukis.entities.hearthstone.*
import com.santukis.viewmodels.R

data class CardFilterState(
    private val metadata: Metadata? = null,
    val cardFilters: Map<Int, List<CardFilter<*>>> = emptyMap(),
    private val activeFilters: Map<Int, CardFilter<*>?> = mapOf()
) {

    companion object {
        const val CARD_SET = 1
        const val GAME_MODE = 2
        const val CARD_TYPE = 3
        const val RARITY = 4
        const val CARD_CLASS = 5
        const val MINION_TYPE = 6
        const val SPELL_SCHOOL = 7
        const val KEYWORD = 8
        const val CARD_STATS = 9
    }

    fun getCardClasses(): List<CardClass> = metadata?.classes.orEmpty()

    fun getSelectedCardClassDrawable(): Int =
        (activeFilters[CARD_CLASS]?.getFilter() as? CardClass)?.getDrawable() ?: R.drawable.no_class

    fun getFilterColor(key: Int, filter: CardFilter<*>): Color {
        val selectedFilter = activeFilters[key]

        return if (selectedFilter?.getId() == filter.getId()) {
            filter.getSelectedColor()

        } else {
            filter.getUnselectedColor()
        }
    }

    fun getActiveFilters(): List<Pair<Int, CardFilter<*>>> = activeFilters.filterNotNullValues().toList()

    fun updateActiveFilters(key: Int, filter: CardFilter<*>?): Map<Int, CardFilter<*>?> =
        activeFilters.toMutableMap().apply { set(key, filter) }

    fun buildSearchCardsRequest(searchCardsRequest: SearchCardsRequest): SearchCardsRequest {
        return searchCardsRequest.copy(
            cardClass = activeFilters[CARD_CLASS]?.getFilter() as? CardClass,
            set = activeFilters[CARD_SET]?.getFilter() as? CardSet,
            rarity = activeFilters[RARITY]?.getFilter() as? Rarity,
            spellSchool = activeFilters[SPELL_SCHOOL]?.getFilter() as? SpellSchool,
            type = activeFilters[CARD_TYPE]?.getFilter() as? CardType
        )
    }
}