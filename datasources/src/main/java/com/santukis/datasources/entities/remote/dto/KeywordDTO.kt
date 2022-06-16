package com.santukis.datasources.entities.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeywordDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "refText")
    val refText: String? = null,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "gameModes")
    val gameModes: List<Int>? = null
)