package com.santukis.datasources.entities.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BattlegroundsDTO(
    @Json(name = "hero")
    val hero: Boolean? = null,

    @Json(name = "companionId")
    val companionId: Int? = null,

    @Json(name = "image")
    val image: String? = null,

    @Json(name = "imageGold")
    val imageGold: String? = null
)