package com.santukis.datasources.authentication

import com.santukis.datasources.entities.dto.ServerResponse.Companion.defaultError
import com.santukis.entities.authentication.Token

interface AuthenticationDataSource {

    fun getToken(): Result<Token> = defaultError<Token>().toResult()

    fun saveToken(token: Token) { }
}