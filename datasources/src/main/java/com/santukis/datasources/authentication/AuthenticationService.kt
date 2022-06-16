package com.santukis.datasources.authentication

import com.santukis.datasources.entities.dto.AuthenticationSuccessDTO
import com.santukis.datasources.remote.HttpClient
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationService {

    @Headers(HttpClient.BASIC_AUTHORIZATION + ": ")
    @Multipart
    @POST
    fun refreshToken(
        @Part(value = "grant_type")
        grantType: String = "client_credentials"
    ): Call<AuthenticationSuccessDTO>
}