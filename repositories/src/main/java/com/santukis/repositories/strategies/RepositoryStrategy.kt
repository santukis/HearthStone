package com.santukis.repositories.strategies

interface RepositoryStrategy<Input, Output> {
    suspend fun execute(input: Input): Result<Output>
}

suspend infix fun <T> Result<T>?.or(alternative: suspend () -> Result<T>): Result<T> =
    if (this == null || isFailure) {
        alternative()
    } else {
        this
    }