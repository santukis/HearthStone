package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardBackDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "sortCategory")
    val sortCategory: Int? = null,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "image")
    val image: String? = null,

    @Json(name = "slug")
    val slug: String? = null
)