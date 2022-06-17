package com.santukis.datasources.remote

import android.util.Base64
import com.santukis.datasources.BuildConfig
import com.santukis.datasources.remote.HttpClient.Companion.AUTHORIZATION
import okhttp3.*

class BasicAuthorization : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addBasicAuthorizationToRequest(chain.request())

        return chain.proceed(request)
    }

    private fun addBasicAuthorizationToRequest(request: Request): Request {
        val credentials = "${BuildConfig.BATTLENET_CLIENT_ID}:${BuildConfig.BATTLENET_CLIENT_SECRET}"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        return request
            .newBuilder()
            .addHeader(AUTHORIZATION, "Basic $encodedCredentials")
            .build()
    }
}