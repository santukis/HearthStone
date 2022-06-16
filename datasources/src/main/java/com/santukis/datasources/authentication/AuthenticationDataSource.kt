package com.santukis.datasources.authentication

import com.santukis.entities.authentication.Token

interface AuthenticationDataSource {

    fun getToken(): Result<Token>
}