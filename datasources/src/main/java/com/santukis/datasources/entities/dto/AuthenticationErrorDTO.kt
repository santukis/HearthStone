package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationErrorDTO(
    @Json(name = "error")
    val error: String? = null,

    @Json(name = "error_description")
    val description: String? = null
)