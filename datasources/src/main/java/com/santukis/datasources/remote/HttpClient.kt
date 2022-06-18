package com.santukis.datasources.remote

import com.santukis.datasources.entities.dto.ServerResponse
import com.santukis.datasources.authentication.AuthenticationService
import com.santukis.datasources.hearthstone.HearthstoneService
import com.santukis.entities.hearthstone.Region
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

sealed class Environment(
    val authApi: String
) {
    private val blizzardApi: String = "https://{region}.api.blizzard.com/"

    class Pro: Environment(authApi = "https://us.battle.net/")

    class Testing(baseUrl: String): Environment(baseUrl)

    fun deckEndpoint(region: Region): String =
        blizzardApi.replace("{region}", region.value) + "hearthstone/deck"
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