package com.santukis.repositories.strategies

abstract class LocalRemoteStrategy<Input, Output> : RepositoryStrategy<Input, Output> {

    protected abstract suspend fun shouldLoadFromLocal(input: Input): Boolean

    protected abstract suspend fun loadFromLocal(input: Input): Result<Output>

    protected abstract suspend fun loadFromRemote(input: Input, localResult: Output?): Result<Output>

    protected abstract suspend fun shouldUpdateFromRemote(input: Input, localOutput: Output): Boolean

    protected abstract suspend fun saveIntoLocal(output: Output)

    override suspend fun execute(input: Input): Result<Output> {
        var result: Result<Output> = defaultError()

        if (shouldLoadFromLocal(input)) {
            result = loadFromLocal(input)
        }

        if (result.isFailure
            || shouldUpdateFromRemote(input, result.getOrThrow())
        ) {
            result = loadFromRemote(input, result.getOrNull())
                .fold(
                    onSuccess = {
                        saveIntoLocal(it)
                        loadFromLocal(input)
                    },
                    onFailure = {
                        Result.failure(it)
                    }
                )
        }

        return result
    }
}