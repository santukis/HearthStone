package com.santukis.datasources.remote

import okhttp3.*

class TokenAuthenticator: Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }
}