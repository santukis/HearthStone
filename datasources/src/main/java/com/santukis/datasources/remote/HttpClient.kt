package com.santukis.datasources.remote

import com.santukis.datasources.entities.dto.ServerResponse
import com.santukis.datasources.BuildConfig
import com.santukis.datasources.authentication.AuthenticationService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class HttpClient(
    environment: Environment,
    client: OkHttpClient
) {

    companion object {
        const val BASIC_AUTHORIZATION = "Add_Basic_Authorization"
        const val AUTHORIZATION = "Authorization"
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(environment.baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val authenticationService: AuthenticationService = retrofit.create()
}

sealed class Environment(val baseUrl: String) {

    class Pro(region: String): Environment("$region.api.blizzard.com/")

    class Testing(baseUrl: String): Environment(baseUrl)
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