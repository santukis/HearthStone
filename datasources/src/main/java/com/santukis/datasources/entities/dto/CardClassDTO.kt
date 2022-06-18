package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.Identity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardClassDTO(
    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "cardId")
    val cardId: Int? = null,

    @Json(name = "heroPowerCardId")
    val heroPowerCardId: Int? = null,

    @Json(name = "alternateHeroCardIds")
    val alternateHeroCardIds: List<Int>? = null
) {

    fun toCardClass(): CardClass =
        CardClass(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            ),
            cardId = cardId.orDefault(),
            heroPowerCardId = heroPowerCardId.orDefault()
        )
}