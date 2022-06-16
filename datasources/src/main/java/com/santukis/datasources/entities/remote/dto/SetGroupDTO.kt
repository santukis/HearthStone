package com.santukis.datasources.entities.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SetGroupDTO(
    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "year")
    val year: Int? = null,

    @Json(name = "svg")
    val svg: String? = null,

    @Json(name = "cardSets")
    val cardSets: List<String>? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "standard")
    val standard: Boolean? = null,

    @Json(name = "icon")
    val icon: String? = null,

    @Json(name = "yearRange")
    val yearRange: String? = null
)