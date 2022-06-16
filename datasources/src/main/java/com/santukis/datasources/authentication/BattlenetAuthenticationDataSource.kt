package com.santukis.datasources.authentication

import com.santukis.entities.authentication.Token
import com.santukis.datasources.entities.dto.AuthenticationErrorDTO
import com.santukis.datasources.entities.dto.AuthenticationSuccessDTO
import com.santukis.datasources.remote.unwrapCall

class BattlenetAuthenticationDataSource(
    private val authenticationService: AuthenticationService
) : AuthenticationDataSource {

    override fun getToken(): Result<Token> {
        return authenticationService
            .refreshToken()
            .unwrapCall<AuthenticationErrorDTO, AuthenticationSuccessDTO, Token>(
                onSuccess = { successDto ->
                    Token(
                        accessToken = successDto.accessToken.orEmpty(),
                        expires = successDto.expiresIn?.let { System.currentTimeMillis() + it } ?: 0L
                    )
                },
                onError = { errorDTO -> Exception(errorDTO.description) }
            )
    }
}