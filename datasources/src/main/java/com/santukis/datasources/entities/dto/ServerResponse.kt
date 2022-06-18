package com.santukis.datasources.entities.dto

sealed class ServerResponse<out Response> {

    class Success<out Response>(val data: Response): ServerResponse<Response>()

    class Error<out Response>(val error: Throwable): ServerResponse<Response>()

    fun toResult(): Result<Response> = when (this) {
        is Success -> Result.success(data)
        is Error -> Result.failure(error)
    }
}