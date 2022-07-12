package com.santukis.entities.hearthstone

data class Keyword(
    val identity: Identity = Identity(),
    val cardText: CardText = CardText(),
    val gameModes: List<GameMode> = emptyList()
) {

    fun getName(): String = identity.name
}