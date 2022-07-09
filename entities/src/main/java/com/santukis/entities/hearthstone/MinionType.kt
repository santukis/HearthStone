package com.santukis.entities.hearthstone

data class MinionType(
    val identity: Identity = Identity(),
    val gameModes: List<GameMode> = emptyList()
)