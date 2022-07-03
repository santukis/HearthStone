package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.CardStats
import com.santukis.entities.hearthstone.Metadata

data class CardFilterState(
    private val metadata: Metadata? = null,
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
}