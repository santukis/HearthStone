package com.santukis.entities.hearthstone

data class Rarity(
    val identity: Identity = Identity(),
    val craftingCost: List<Int> = emptyList(),
    val dustValue: List<Int> = emptyList()
)