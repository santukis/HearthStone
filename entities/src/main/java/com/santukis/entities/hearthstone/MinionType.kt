package com.santukis.entities.hearthstone

class MinionType(
    val identity: Identity = Identity(),
    val gameModes: List<GameMode> = emptyList()
)