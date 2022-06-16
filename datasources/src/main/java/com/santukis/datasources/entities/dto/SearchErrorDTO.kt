package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchErrorDTO(
    @Json(name = "error")
    val error: ErrorDTO? = null
)