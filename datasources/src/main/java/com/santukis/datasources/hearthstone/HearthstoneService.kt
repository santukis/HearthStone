package com.santukis.datasources.hearthstone

import com.santukis.datasources.entities.dto.DeckResponseDTO
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
    ): Call<DeckResponseDTO>
}