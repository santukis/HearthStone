package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorDTO(
    @Json(name = "status")
    val status: Int? = null,

    @Json(name = "statusMessage")
    val statusMessage: String? = null,

    @Json(name = "message")
    val message: String? = null
)