package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.datasources.remote.PagingData
import com.santukis.entities.hearthstone.Card
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardsResponse(
    @Json(name = "cards")
    val cards: List<CardDTO>? = null,

    @Json(name = "cardCount")
    val cardCount: Int? = null,

    @Json(name = "pageCount")
    val pageCount: Int? = null,

    @Json(name = "page")
    val page: Int? = null
) {

    fun toCardList(): List<Card> = cards?.map { it.toCard() }.orEmpty()

    fun toPagingData(pageSize: Int) =
        PagingData(
            itemCount = cardCount.orDefault(),
            pageCount = pageCount.orDefault(),
            currentPage = page.orDefault(),
            pageSize = pageSize
        )
}