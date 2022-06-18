package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.entities.hearthstone.CardSet
import com.santukis.entities.hearthstone.Identity
import com.santukis.entities.hearthstone.SetType
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
) {

    fun toCardSet(): CardSet =
        CardSet(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            ),
            type = toSetType(),
            collectibleCount = collectibleCount.orDefault(),
        )

    private fun toSetType(): SetType =
        when (type) {
            "base" -> SetType.Base
            "adventure" -> SetType.Adventure
            "expansion" -> SetType.Expansion
            else -> SetType.Unknown
        }
}