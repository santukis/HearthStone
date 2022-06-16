package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SetDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "collectibleCount")
    val collectibleCount: Int? = null,

    @Json(name = "collectibleRevealedCount")
    val collectibleRevealedCount: Int? = null,

    @Json(name = "nonCollectibleCount")
    val nonCollectibleCount: Int? = null,

    @Json(name = "nonCollectibleRevealedCount")
    val nonCollectibleRevealedCount: Int? = null,

    @Json(name = "aliasSetIds")
    val aliasSetIds: List<Int>? = null
)