package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DuelsDTO(
    @Json(name = "relevant")
    val relevant: Boolean? = null,

    @Json(name = "constructed")
    val constructed: Boolean? = null
)