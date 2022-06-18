package com.santukis.datasources.authentication

import com.santukis.entities.authentication.Token
import com.santukis.datasources.entities.dto.AuthenticationErrorDTO
import com.santukis.datasources.entities.dto.AuthenticationSuccessDTO
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.unwrapCall

class BattlenetAuthenticationDataSource(private val client: HttpClient) : AuthenticationDataSource {

    override fun getToken(): Result<Token> {
        return client.authenticationService
            .refreshToken()
            .unwrapCall<AuthenticationErrorDTO, AuthenticationSuccessDTO, Token>(
                onSuccess = { successDto -> successDto.toToken() },
                onError = { errorDTO -> Exception(errorDTO.description) }
            )
    }
}