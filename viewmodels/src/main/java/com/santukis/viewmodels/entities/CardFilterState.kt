package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.Metadata

data class CardFilterState(
    private val metadata: Metadata? = null,
    private val selectedCardClass: CardClass? = null,
    val shouldShowCardClassList: Boolean = false
) {

    fun shouldShowCardClass(): Boolean = selectedCardClass != null

    fun getCardClasses(): List<CardClass> = metadata?.classes.orEmpty()

    fun getSelectedCardClassDrawable(): Int = selectedCardClass.getDrawable()
}