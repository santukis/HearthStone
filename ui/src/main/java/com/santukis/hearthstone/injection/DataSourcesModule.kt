package com.santukis.hearthstone.injection

import com.santukis.datasources.BuildConfig
import com.santukis.datasources.authentication.AuthenticationDataSource
import com.santukis.datasources.authentication.BattlenetAuthenticationDataSource
import com.santukis.datasources.authentication.EncryptedAuthenticationDataSource
import com.santukis.datasources.hearthstone.BattlenetHearthstoneDataSource
import com.santukis.datasources.hearthstone.RoomHearthstoneDataSource
import com.santukis.datasources.local.HearthstoneDatabase
import com.santukis.datasources.remote.BasicAuthorization
import com.santukis.datasources.remote.Environment
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.TokenAuthorization
import com.santukis.hearthstone.injection.DataSourceConstants.AUTHENTICATION_CLIENT
import com.santukis.hearthstone.injection.DataSourceConstants.AUTHENTICATION_MODULE_NAME
import com.santukis.hearthstone.injection.DataSourceConstants.BATTLENET_DATA_SOURCE
import com.santukis.hearthstone.injection.DataSourceConstants.DATABASE_MODULE_NAME
import com.santukis.hearthstone.injection.DataSourceConstants.DATA_SOURCES_MODULE_NAME
import com.santukis.hearthstone.injection.DataSourceConstants.ENCRYPTED_DATA_SOURCE
import com.santukis.hearthstone.injection.DataSourceConstants.HEARTHSTONE_CLIENT
import com.santukis.hearthstone.injection.DataSourceConstants.HEARTHSTONE_MODULE_NAME
import com.santukis.hearthstone.injection.DataSourceConstants.HTTP_CLIENT_MODULE_NAME
import com.santukis.hearthstone.injection.DataSourceConstants.LOGGING_INTERCEPTOR
import com.santukis.hearthstone.injection.DataSourceConstants.ROOM_DATA_SOURCE
import com.santukis.repositories.hearthstone.HearthstoneDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.kodein.di.*

object DataSourceConstants {
    const val DATA_SOURCES_MODULE_NAME = "dataSources"
    const val AUTHENTICATION_MODULE_NAME = "authentication"
    const val HEARTHSTONE_MODULE_NAME = "hearthstone"
    const val DATABASE_MODULE_NAME = "database"
    const val HTTP_CLIENT_MODULE_NAME = "httpClient"
    const val BATTLENET_DATA_SOURCE = "battlenet"
    const val ENCRYPTED_DATA_SOURCE = "encrypted"
    const val ROOM_DATA_SOURCE = "room"
    const val AUTHENTICATION_CLIENT = "authenticationClient"
    const val HEARTHSTONE_CLIENT = "hearthstoneApiClient"
    const val LOGGING_INTERCEPTOR = "loggingInterceptor"
}

fun dataSources() = DI.Module(
    name = DATA_SOURCES_MODULE_NAME,
    allowSilentOverride = true
) {
    import(authentication(), allowOverride = true)
    import(hearthstone(), allowOverride = true)
    import(dataBase(), allowOverride = true)
    import(httpClient(), allowOverride = true)
}

fun authentication() = DI.Module(
    name = AUTHENTICATION_MODULE_NAME,
    allowSilentOverride = true
) {
    bind<AuthenticationDataSource>(tag = BATTLENET_DATA_SOURCE) with provider { BattlenetAuthenticationDataSource(instance(AUTHENTICATION_CLIENT)) }
    bind<AuthenticationDataSource>(tag = ENCRYPTED_DATA_SOURCE) with provider { EncryptedAuthenticationDataSource(instance()) }
}

fun hearthstone() = DI.Module(
    name = HEARTHSTONE_MODULE_NAME,
    allowSilentOverride = true
) {
    bind<HearthstoneDataSource>(tag = BATTLENET_DATA_SOURCE) with provider { BattlenetHearthstoneDataSource(instance(HEARTHSTONE_CLIENT)) }
    bind<HearthstoneDataSource>(tag = ROOM_DATA_SOURCE) with singleton { RoomHearthstoneDataSource(instance()) }
}

fun dataBase() = DI.Module(
    name = DATABASE_MODULE_NAME,
    allowSilentOverride = true
) {
    bind<HearthstoneDatabase>() with eagerSingleton { HearthstoneDatabase.getInstance(instance()) }
}

fun httpClient() = DI.Module(
    name = HTTP_CLIENT_MODULE_NAME,
    allowSilentOverride = true
) {
    bind<Environment>() with singleton { Environment.Pro() }

    bind<Interceptor>(tag = LOGGING_INTERCEPTOR) with singleton {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }
    }

    bind<OkHttpClient>(tag = AUTHENTICATION_CLIENT) with singleton {
        OkHttpClient.Builder()
            .addInterceptor(interceptor = instance(tag = LOGGING_INTERCEPTOR))
            .addInterceptor(interceptor = BasicAuthorization())
            .build()
    }

    bind<OkHttpClient>(tag = HEARTHSTONE_CLIENT) with singleton {
        val tokenAuthorization = TokenAuthorization(
            instance(BATTLENET_DATA_SOURCE),
            instance(ENCRYPTED_DATA_SOURCE)
        )

        OkHttpClient.Builder()
            .addInterceptor(interceptor = instance(tag = LOGGING_INTERCEPTOR))
            .addInterceptor(tokenAuthorization)
            .authenticator(tokenAuthorization)
            .build()
    }

    bind<HttpClient>(tag = AUTHENTICATION_CLIENT) with singleton { HttpClient(instance(), instance(tag = AUTHENTICATION_CLIENT)) }

    bind<HttpClient>(tag = HEARTHSTONE_CLIENT) with singleton { HttpClient(instance(), instance(tag = HEARTHSTONE_CLIENT)) }
}