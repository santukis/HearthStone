package com.santukis.repositories.strategies

interface RepositoryStrategy<Input, Output> {
    suspend fun execute(input: Input): Result<Output>
}

suspend infix fun <T> Result<T>?.or(alternative: suspend () -> Result<T>): Result<T> =
    this?.takeIf { isSuccess } ?: alternative()

fun <Response> defaultError(): Result<Response> = Result.failure(Exception("UNIMPLEMENTED_METHOD"))