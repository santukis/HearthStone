package com.santukis.datasources.remote


import com.santukis.datasources.authentication.AuthenticationDataSource
import com.santukis.datasources.remote.HttpClient.Companion.AUTHORIZATION
import okhttp3.*

class TokenAuthorization(
    private val remoteAuthenticationDataSource: AuthenticationDataSource,
    private val localAuthenticationDataSource: AuthenticationDataSource
) : Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addBearerTokenToRequest(chain.request())

        return chain.proceed(request)
    }

    private fun addBearerTokenToRequest(request: Request): Request {
        val result = localAuthenticationDataSource.getToken()

        return when {
            result.isSuccess -> request
                .newBuilder()
                .removeHeader(AUTHORIZATION)
                .addHeader(AUTHORIZATION, "Bearer ${result.getOrNull()?.accessToken}")
                .build()

            else -> request
        }
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val result = remoteAuthenticationDataSource.getToken()

        return result.takeIf { it.isSuccess }?.let {
            localAuthenticationDataSource.saveToken(it.getOrThrow())
            addBearerTokenToRequest(response.request)
        }
    }
}