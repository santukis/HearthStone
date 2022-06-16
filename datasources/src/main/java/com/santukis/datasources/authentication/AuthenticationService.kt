package com.santukis.datasources.authentication

import com.santukis.datasources.entities.dto.AuthenticationSuccessDTO
import com.santukis.datasources.remote.HttpClient
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationService {

    @Headers(HttpClient.BASIC_AUTHORIZATION + ": ")
    @Multipart
    @POST("oauth/token")
    fun refreshToken(
        @Part(value = "grant_type")
        grantType: String = "client_credentials"
    ): Call<AuthenticationSuccessDTO>
}