package com.santukis.datasources.authentication

import com.santukis.entities.authentication.Token
import com.santukis.repositories.strategies.defaultError

interface AuthenticationDataSource {

    fun getToken(): Result<Token> = defaultError()

    fun saveToken(token: Token) { }
}