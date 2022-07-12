package com.santukis.viewmodels.entities

sealed class UiEvent

data class OnCardSelected(val cardIndex: Int): UiEvent()

data class OnFilterSelected(val key: Int, val filter: CardFilter<*>): UiEvent()

data class OnFilterRemoved(val key: Int): UiEvent()

class OnFavouriteClick(): UiEvent()

data class OnEndReached(val lastItemPosition: Int): UiEvent()
