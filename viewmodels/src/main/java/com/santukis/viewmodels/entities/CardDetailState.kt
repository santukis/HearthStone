package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.Deck

data class CardDetailState(
    val isLoading: Boolean = false,
    val card: Card? = null,
    val relatedCards: List<Card> = emptyList(),
    val errorMessage: String = ""
)