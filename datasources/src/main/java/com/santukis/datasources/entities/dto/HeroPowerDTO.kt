package com.santukis.datasources.entities.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeroPowerDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "collectible")
    val collectible: Int? = null,

    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "classId")
    val classId: Int? = null,

    @Json(name = "multiClassIds")
    val multiClassIds: List<Int>? = null,

    @Json(name = "cardTypeId")
    val cardTypeId: Int? = null,

    @Json(name = "cardSetId")
    val cardSetId: Int? = null,

    @Json(name = "rarityId")
    val rarityId: Int? = null,

    @Json(name = "artistName")
    val artistName: String? = null,

    @Json(name = "manaCost")
    val manaCost: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "image")
    val image: String? = null,

    @Json(name = "imageGold")
    val imageGold: String? = null,

    @Json(name = "flavorText")
    val flavorText: String? = null,

    @Json(name = "cropImage")
    val cropImage: String? = null,

    @Json(name = "parentId")
    val parentId: Int? = null
)