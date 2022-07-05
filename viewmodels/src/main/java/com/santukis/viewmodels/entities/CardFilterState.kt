package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.CardStats
import com.santukis.entities.hearthstone.Metadata
import com.santukis.entities.hearthstone.Rarity

data class CardFilterState(
    private val metadata: Metadata? = null,
    private val activeFilters: Map<Int, FilterUI<Int, String>> = mapOf(),
    val selectedCardClass: CardClass? = null,
    val selectedCardStats: CardStats = CardStats(),
    val selectedCardRarity: Rarity? = null,
    val shouldShowCardClassList: Boolean = false
) {

    companion object {
        const val UNSELECTED = "-1"
    }

    fun getCardClasses(): List<CardClass> = metadata?.classes.orEmpty()

    fun getRarities(): List<Rarity> = metadata?.rarities.orEmpty()

    fun getSelectedCardClassDrawable(): Int = selectedCardClass.getDrawable()

    fun getManaCostColor(cost: Int): Color =
        if (selectedCardStats.manaCost == cost) {
            Color.Yellow

        } else {
            Color.White
        }

    fun getRarityNameColor(rarity: Rarity): Color =
        if (selectedCardRarity == rarity) {
            Color.Blue

        } else {
            Color.Unspecified
        }

    fun updateActiveFilters(filterUI: FilterUI<Int, String>): Map<Int, FilterUI<Int, String>> {
        val updatedFilters = when (filterUI.value) {
            UNSELECTED -> activeFilters.filterNot { it.key == filterUI.key }
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