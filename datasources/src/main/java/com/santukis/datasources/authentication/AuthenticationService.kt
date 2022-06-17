package com.santukis.datasources.authentication

import com.santukis.datasources.entities.dto.AuthenticationSuccessDTO
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationService {

    @Multipart
    @POST("oauth/token")
    fun refreshToken(
        @Part(value = "grant_type")
        grantType: RequestBody = "client_credentials".toRequestBody()
    ): Call<AuthenticationSuccessDTO>
}