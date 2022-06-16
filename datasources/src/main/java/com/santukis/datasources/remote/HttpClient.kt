package com.santukis.datasources.remote

import com.santukis.datasources.entities.dto.ServerResponse
import com.santukis.datasources.authentication.AuthenticationService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HttpClient(
    environment: Environment,
    client: OkHttpClient
) {

    companion object {
        const val BASIC_AUTHORIZATION = "Add_Basic_Authorization"
        const val AUTHORIZATION = "Authorization"
    }

    private val authRetrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(environment.authApi)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val blizzardRetrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(environment.blizzardApi)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val authenticationService: AuthenticationService = authRetrofit.create(AuthenticationService::class.java)
}

sealed class Environment(
    val authApi: String,
    val blizzardApi: String
    ) {

    class Pro: Environment(authApi = "https://us.battle.net/", blizzardApi = "https://eu.api.blizzard.com/")

    class Testing(baseUrl: String): Environment(baseUrl, baseUrl)
}

inline fun <reified ErrorDTO, reified SuccessDTO, SuccessResult> Call<SuccessDTO>.unwrapCall(
    onSuccess: (SuccessDTO) -> SuccessResult,
    onError: (ErrorDTO) -> Throwable
): Result<SuccessResult> =
    try {
        execute().let { response ->
            when (response.isSuccessful) {
                true -> {
                    val successDTO = response.body() ?: throw Exception("No body to parse")
                    ServerResponse.Success(onSuccess(successDTO)).toResult()
                }
                false -> {
                    val errorDTO = Moshi.Builder()
                        .build()
                        .adapter(ErrorDTO::class.java)
                        .fromJson(response.errorBody()?.source()) ?: throw Exception("No errorBody to parse")

                    ServerResponse.Error<SuccessResult>(onError(errorDTO)).toResult()
                }
            }
        }

    } catch (exception: Exception) {
        ServerResponse.Error<SuccessResult>(exception).toResult()
    }