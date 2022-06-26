package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.Deck

data class CardCollectionState(
    val isLoading: Boolean = false,
    val cards: List<Card> = emptyList(),
    val selectedCard: Card? = null,
    val errorMessage: String = ""
)