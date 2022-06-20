package com.santukis.datasources.remote

import com.santukis.datasources.remote.services.AuthenticationService
import com.santukis.datasources.remote.services.HearthstoneService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HttpClient(
    val environment: Environment,
    client: OkHttpClient
) {

    companion object {
        const val AUTHORIZATION = "Authorization"
    }

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())

    val authenticationService: AuthenticationService = retrofitBuilder
        .baseUrl(environment.authApi)
        .build()
        .create(AuthenticationService::class.java)

    val hearthstoneService: HearthstoneService = retrofitBuilder
        .build()
        .create(HearthstoneService::class.java)
}

inline fun <reified ErrorDTO, reified SuccessDTO, SuccessResult> Call<SuccessDTO>.unwrapCall(
    onSuccess: (SuccessDTO) -> SuccessResult,
    onError: (ErrorDTO) -> Throwable
): Result<SuccessResult> =
    try {
        execute().let { response ->
            if (response.isSuccessful) {
                val successDTO = response.body() ?: throw Exception("No body to parse")
                Result.success(onSuccess(successDTO))

            } else {
                val errorDTO = Moshi.Builder()
                    .build()
                    .adapter(ErrorDTO::class.java)
                    .fromJson(response.errorBody()?.source()) ?: throw Exception("No errorBody to parse")

                Result.failure(onError(errorDTO))
            }
        }

    } catch (exception: Exception) {
        Result.failure(exception)
    }