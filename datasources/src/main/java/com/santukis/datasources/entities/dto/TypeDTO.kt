package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.datasources.mappers.toGameModeList
import com.santukis.datasources.mappers.toSimplifiedIdentity
import com.santukis.entities.hearthstone.CardType
import com.santukis.entities.hearthstone.GameMode
import com.santukis.entities.hearthstone.Identity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TypeDTO(
    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "gameModes")
    val gameModes: List<Int>? = null
) {

    fun toCardType(): CardType =
        CardType(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            ),
            gameModes = gameModes.toGameModeList()
        )
}