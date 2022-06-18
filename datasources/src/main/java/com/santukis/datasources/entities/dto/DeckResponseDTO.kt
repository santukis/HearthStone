package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
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
    val hero: HeroDTO? = HeroDTO(),

    @Json(name = "heroPower")
    val heroPower: HeroPowerDTO? = HeroPowerDTO(),

    @Json(name = "class")
    val heroClass: HeroClassDTO? = HeroClassDTO(),

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
            cardCount = cardCount.orDefault()
        )
}