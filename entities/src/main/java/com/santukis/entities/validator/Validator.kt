package com.santukis.entities.validator

operator fun <T> Result<T>.plus(other: (Result<T>) -> Result<T>): Result<T> =
    when (this.isFailure) {
        true -> this
        else -> other(this)
    }

fun <T> validate(
    item: T?,
    onError: () -> String
): Result<T> =
    item?.let { Result.success(item) } ?: Result.failure(Exception(onError()))

object Validations {

    fun <T> Result<T>.isNotEmpty(
        provider: (T) -> String,
        onError: () -> String
    ): Result<T> =
        fold(
            onSuccess = {
                when (provider(it).isNotEmpty()) {
                    true -> this
                    else -> Result.failure(Exception(onError()))
                }
            },
            onFailure = { this }
        )

    fun <T> Result<T>.matches(
        provider: (T) -> String,
        expression: Regex,
        onError: () -> String
    ): Result<T> =
        fold(
            onSuccess = {
                when (provider(it).matches(expression)) {
                    true -> this
                    else -> Result.failure(Exception(onError()))
                }
            },
            onFailure = { this }
        )

    fun <T> Result<T>.startsWith(
        provider: (T) -> String,
        value: String,
        onError: () -> String
    ): Result<T> =
        fold(
            onSuccess = {
                when (provider(it).startsWith(value)) {
                    true -> this
                    else -> Result.failure(Exception(onError()))
                }
            },
            onFailure = { this }
        )

    fun <T> Result<T>.minLength(
        provider: (T) -> String,
        length: Int,
        onError: () -> String
    ): Result<T> =
        fold(
            onSuccess = {
                when (provider(it).length > length) {
                    true -> this
                    else -> Result.failure(Exception(onError()))
                }
            },
            onFailure = { this }
        )

}