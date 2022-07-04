package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.CardStats
import com.santukis.entities.hearthstone.Metadata

data class CardFilterState(
    private val metadata: Metadata? = null,
    private val activeFilters: Map<Int, FilterUI<Int, String>> = mapOf(),
    val selectedCardClass: CardClass? = null,
    val selectedCardStats: CardStats = CardStats(),
    val shouldShowCardClassList: Boolean = false
) {

    fun shouldShowCardClass(): Boolean = selectedCardClass != null

    fun getCardClasses(): List<CardClass> = metadata?.classes.orEmpty()

    fun getSelectedCardClassDrawable(): Int = selectedCardClass.getDrawable()

    fun getManaCostColor(cost: Int): Color =
        if (selectedCardStats.manaCost == cost) {
            Color.Yellow
        } else {
            Color.White
        }

    fun updateActiveFilters(filterUI: FilterUI<Int, String>): Map<Int, FilterUI<Int, String>> {
        val updatedFilters = when (filterUI.value) {
            "-1" -> activeFilters.filterNot { it.key == filterUI.key }
            else -> activeFilters.toMutableMap().apply {
                set(filterUI.key, filterUI)
            }
        }

        return updatedFilters
    }

    fun getActiveFilters(): List<FilterUI<Int, String>> {
        return activeFilters.values.toList()
    }
}