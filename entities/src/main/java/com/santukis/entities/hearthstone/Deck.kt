package com.santukis.entities.hearthstone

data class Deck(
    val code: String = "",
    val version: Int = 0,
    val format: String = "",
    val hero: Card = Card(),
    val heroPower: Card = Card(),
    val cardClass: CardClass = CardClass(),
    val cards: List<Card> = emptyList(),
    val cardCount: Int = 0
)