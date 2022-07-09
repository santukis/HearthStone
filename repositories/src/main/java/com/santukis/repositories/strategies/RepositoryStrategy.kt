package com.santukis.repositories.strategies

import com.santukis.entities.exceptions.NoMoreData
import kotlinx.coroutines.flow.FlowCollector

interface RepositoryStrategy<Input, Output> {
    suspend fun execute(input: Input): Result<Output>
}

suspend infix fun <T> Result<T>?.or(alternative: suspend () -> Result<T>): Result<T> =
    this?.takeIf { isSuccess } ?: alternative()

suspend fun <T> Result<T>.emitIfSuccess(flowCollector: FlowCollector<Result<T>>): Result<T> {
    if (isSuccess) {
        flowCollector.emit(this)
    }

    return this
}

fun <Response> defaultError(): Result<Response> = Result.failure(Exception("UNIMPLEMENTED_METHOD"))

fun <Response> noMoreData(): Result<Response> = Result.failure(NoMoreData("No more Data"))