package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.datasources.mappers.toGameModeList
import com.santukis.entities.hearthstone.CardText
import com.santukis.entities.hearthstone.Identity
import com.santukis.entities.hearthstone.Keyword
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeywordDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "refText")
    val refText: String? = null,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "gameModes")
    val gameModes: List<Int>? = null
) {

    fun toKeyword(): Keyword =
        Keyword(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            ),
            cardText = CardText(
                referenceText = refText.orEmpty(),
                ruleText = text.orEmpty()
            ),
            gameModes = gameModes.toGameModeList()
        )
}