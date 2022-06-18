package com.santukis.entities.hearthstone

data class HeroClass(
    val identity: Identity = Identity(),
    val cardId: Int = -1,
    val heroPowerCardId: Int = -1,
    val alternativeHeroCardIds: List<Int> = emptyList()
)