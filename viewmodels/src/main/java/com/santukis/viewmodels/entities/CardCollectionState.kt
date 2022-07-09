package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card

data class CardCollectionState(
    val cards: List<Card> = emptyList()
) {

    fun reset(): CardCollectionState = copy(cards = emptyList())
}