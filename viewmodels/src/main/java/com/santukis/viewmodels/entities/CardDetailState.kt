package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card

data class CardDetailState(
    val card: Card? = null,
    val relatedCards: List<Card> = emptyList()
)