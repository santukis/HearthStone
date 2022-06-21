package com.santukis.repositories.strategies

abstract class RemoteStrategy<Input, Output>: RepositoryStrategy<Input, Output> {

    protected abstract suspend fun loadFromRemote(input: Input): Result<Output>

    protected abstract suspend fun saveIntoLocal(output: Output): Result<Output>

    override suspend fun execute(input: Input): Result<Output> {
        val remoteResponse = loadFromRemote(input)

        return when {
            remoteResponse.isSuccess -> saveIntoLocal(remoteResponse.getOrThrow())
            else -> remoteResponse
        }
    }
}