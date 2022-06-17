package com.santukis.hearthstone.injection

import com.santukis.datasources.BuildConfig
import com.santukis.datasources.authentication.AuthenticationDataSource
import com.santukis.datasources.authentication.BattlenetAuthenticationDataSource
import com.santukis.datasources.authentication.EncryptedAuthenticationDataSource
import com.santukis.datasources.remote.BasicAuthorization
import com.santukis.datasources.remote.Environment
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.TokenAuthorization
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.kodein.di.*

fun dataSources() = DI.Module(
    name = "dataSources",
    allowSilentOverride = true
) {
    import(authentication(), allowOverride = true)
    import(httpClient(), allowOverride = true)
}

fun authentication() = DI.Module(
    name = "authentication",
    allowSilentOverride = true
) {
    bind<AuthenticationDataSource>(tag = "battlenet") with provider { BattlenetAuthenticationDataSource(instance("authenticationClient")) }
    bind<AuthenticationDataSource>(tag = "encrypted") with provider { EncryptedAuthenticationDataSource(instance()) }
}

fun httpClient() = DI.Module(
    name = "httpClient",
    allowSilentOverride = true
) {
    bind<Environment>() with singleton { Environment.Pro() }

    bind<Interceptor>(tag = "loggingInterceptor") with singleton {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }
    }

    bind<OkHttpClient>(tag = "authenticationClient") with singleton {
        OkHttpClient.Builder()
            .addInterceptor(interceptor = BasicAuthorization())
            .addInterceptor(interceptor = instance(tag = "loggingInterceptor"))
            .build()
    }

    bind<OkHttpClient>(tag = "hearthstoneApiClient") with singleton {
        OkHttpClient.Builder()
            .addInterceptor(interceptor = TokenAuthorization(instance("battlenet"), instance("encrypted")))
            .addInterceptor(interceptor = instance(tag = "loggingInterceptor"))
            .build()
    }

    bind<HttpClient>(tag = "authenticationClient") with singleton { HttpClient(instance(), instance(tag = "authenticationClient")) }

    bind<HttpClient>(tag = "hearthstoneApiClient") with singleton { HttpClient(instance(), instance(tag = "hearthstoneApiClient")) }
}