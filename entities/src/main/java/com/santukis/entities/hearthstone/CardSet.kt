package com.santukis.entities.hearthstone

data class CardSet(
    val identity: Identity = Identity(),
    val type: SetType = SetType.Unknown,
    val collectibleCount: Int? = null
)