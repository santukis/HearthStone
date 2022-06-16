package com.santukis.datasources.entities.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardsResponse(
    @Json(name = "cards")
    val cards: List<CardDTO>? = null,

    @Json(name = "cardCount")
    val cardCount: Int? = null,

    @Json(name = "pageCount")
    val pageCount: Int? = null,

    @Json(name = "page")
    val page: Int? = null
)