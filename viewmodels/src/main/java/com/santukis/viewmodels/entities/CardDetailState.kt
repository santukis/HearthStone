package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card

data class CardDetailState(
    val card: Card? = null,
    val cardIndex: Int = 0,
    val relatedCards: List<Card> = emptyList()
) {

    fun getRarityDrawable(): Int = card?.rarity.getDrawable()

    fun getRarityText(): TextUI = card?.rarity.getText()

    fun getManaCost(): String = card?.cardStats?.getManaCost().orEmpty()

    fun getName(): String = card?.getName().orEmpty()

    fun getFlavorText(): String = card?.cardText?.flavorText.orEmpty()

    fun reset(): CardDetailState =
        copy(
            card = null,
            cardIndex = 0,
            relatedCards = emptyList()
        )
}