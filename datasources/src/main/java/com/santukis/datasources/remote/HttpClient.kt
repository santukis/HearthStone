package com.santukis.datasources.remote

import com.santukis.datasources.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HttpClient(environment: Environment) {

    private val authenticator: TokenAuthenticator = TokenAuthenticator()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authenticator)
        .authenticator(authenticator)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(environment.baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

}

sealed class Environment(val baseUrl: String) {

    class Pro(region: String): Environment("$region.api.blizzard.com/")

    class Testing(baseUrl: String): Environment(baseUrl)
}