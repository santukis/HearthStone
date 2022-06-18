package com.santukis.datasources.entities.dto

import com.santukis.datasources.mappers.orDefault
import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.Deck
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeckResponseDTO(
    @Json(name = "deckCode")
    val deckCode: String? = "",

    @Json(name = "version")
    val version: Int? = 0,

    @Json(name = "format")
    val format: String? = "",

    @Json(name = "hero")
    val hero: CardDTO? = null,

    @Json(name = "heroPower")
    val heroPower: CardDTO? = null,

    @Json(name = "class")
    val cardClass: CardClassDTO? = null,

    @Json(name = "cards")
    val cards: List<CardDTO>? = listOf(),

    @Json(name = "cardCount")
    val cardCount: Int? = 0
) {

    fun toDeck(): Deck =
        Deck(
            code = deckCode.orEmpty(),
            version = version.orDefault(),
            format = format.orEmpty(),
            hero = hero?.toCard() ?: Card(),
            heroPower = heroPower?.toCard() ?: Card(),
            cardClass = cardClass?.toCardClass() ?: CardClass(),
            cards = cards?.map { it.toCard() }.orEmpty(),
            cardCount = cardCount.orDefault()
        )
}