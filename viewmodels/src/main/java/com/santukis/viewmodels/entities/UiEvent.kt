package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.CardClass

sealed class UiEvent

data class OnCardSelected(val cardIndex: Int): UiEvent()

data class OnCardClassSelected(val cardClass: CardClass): UiEvent()

data class OnFilterSelected(val key: Int, val filter: CardFilter<*>): UiEvent()

data class OnFilterRemoved(val key: Int): UiEvent()

class OnFavouriteClick(): UiEvent()

data class OnEndReached(val lastItemPosition: Int): UiEvent()
