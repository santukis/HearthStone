package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RarityDTO(
    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "craftingCost")
    val craftingCost: List<Int>? = null,

    @Json(name = "dustValue")
    val dustValue: List<Int>? = null,

    @Json(name = "name")
    val name: String? = null
)