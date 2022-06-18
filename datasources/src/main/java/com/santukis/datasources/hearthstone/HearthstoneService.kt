package com.santukis.datasources.hearthstone

import com.santukis.datasources.entities.dto.CardsResponse
import com.santukis.datasources.entities.dto.DeckResponse
import com.santukis.datasources.entities.dto.MetadataResponse
import retrofit2.Call
import retrofit2.http.*

interface HearthstoneService {

    @GET
    fun getDeck(
        @Url baseUrl: String,
        @Query("locale") locale: String,
        @Query("code", encoded = true) code: String? = null,
        @Query("ids") ids: String? = null,
        @Query("hero") heroId: String? = null
    ): Call<DeckResponse>

    @GET
    fun searchCards(
        @Url baseUrl: String,
        @Query("locale") locale: String,
        @Query("set") set: String? = null,
        @Query("class") classSlug: String? = null,
        @Query("manaCost") manaCost: Int? = null,
        @Query("attack") attack: Int? = null,
        @Query("health") health: Int? = null,
        @Query("collectible") collectible: String? = null,
        @Query("rarity") rarity: String? = null,
        @Query("type") type: String? = null,
        @Query("minionType") minionType: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("textFilter") textFilter: String? = null,
        @Query("gameMode") gameMode: String? = null,
        @Query("spellSchool") spellSchool: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sort") sort: String? = null
    ): Call<CardsResponse>

    @GET
    fun getMetadata(
        @Url baseUrl: String,
        @Query("locale") locale: String
    ): Call<MetadataResponse>
}