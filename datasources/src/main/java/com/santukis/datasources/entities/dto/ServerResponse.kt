package com.santukis.datasources.entities.dto

sealed class ServerResponse<out Response> {

    class Success<out Response>(val data: Response): ServerResponse<Response>()

    class Error<out Response>(val error: Throwable): ServerResponse<Response>()

    fun toResult(): Result<Response> = when (this) {
        is Success -> Result.success(data)
        is Error -> Result.failure(error)
    }

    companion object {
        private const val UNIMPLEMENTED_METHOD = "UNIMPLEMENTED_METHOD"

        fun <Response> defaultError(): Error<Response> = Error(Exception(UNIMPLEMENTED_METHOD))
    }
}