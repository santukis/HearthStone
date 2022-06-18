package com.santukis.entities.hearthstone

data class CardType(
    val identity: Identity = Identity(),
    val gameModes: List<GameMode> = emptyList()
)
