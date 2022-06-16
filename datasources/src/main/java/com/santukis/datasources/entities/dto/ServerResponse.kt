package com.santukis.datasources.entities.dto

sealed class ServerResponse<out Error, out Response> {

    class Success<out Response>(val data: Response): ServerResponse<Nothing, Response>()

    class Error<out Error>(val error: Error): ServerResponse<Error, Nothing>()

    companion object {
        fun <Response> success(data: Response) = Success(data)

        fun <Error> failure(error: Error) = Error(error)
    }
}