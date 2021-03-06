package com.santukis.datasources.entities.dto


import com.santukis.entities.core.orDefault
import com.santukis.entities.authentication.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationSuccessDTO(
    @Json(name = "access_token")
    val accessToken: String? = null,

    @Json(name = "token_type")
    val tokenType: String? = null,

    @Json(name = "expires_in")
    val expiresIn: Int? = null,

    @Json(name = "sub")
    val sub: String? = null
) {

    fun toToken(): Token =
        Token(
            accessToken = accessToken.orEmpty(),
            expires = expiresIn?.toLong().orDefault()
        )
}