package com.santukis.datasources.entities.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClassDTO(
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
)