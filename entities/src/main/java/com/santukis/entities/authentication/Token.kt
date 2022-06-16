package com.santukis.entities.authentication

data class Token(
    val accessToken: String = "",
    val expires: Long = -1
)