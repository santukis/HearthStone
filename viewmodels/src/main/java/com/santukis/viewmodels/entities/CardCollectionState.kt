package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Card

data class CardCollectionState(
    val isLoading: Boolean = false,
    val cards: List<Card> = emptyList(),
    val errorMessage: String = ""
)