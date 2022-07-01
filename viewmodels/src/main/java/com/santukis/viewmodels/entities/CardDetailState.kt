package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card

data class CardDetailState(
    val card: Card? = null,
    val cardIndex: Int = 0,
    val relatedCards: List<Card> = emptyList()
)