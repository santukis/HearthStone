package com.santukis.repositories.strategies

abstract class LocalRemoteStrategy<Input, Output>: RepositoryStrategy<Input, Output> {

    protected abstract suspend fun loadFromLocal(input: Input): Result<Output>

    protected abstract suspend fun loadFromRemote(input: Input): Result<Output>

    protected abstract suspend fun shouldUpdateFromRemote(input: Input, localOutput: Output): Boolean

    protected abstract suspend fun saveIntoLocal(output: Output): Result<Output>

    override suspend fun execute(input: Input): Result<Output> {
        return loadFromLocal(input).takeIf { localResult ->
                localResult.isSuccess && !shouldUpdateFromRemote(input, localResult.getOrThrow())
            } or {
               val remoteResult = loadFromRemote(input)

               if (remoteResult.isSuccess) {
                   saveIntoLocal(remoteResult.getOrThrow())

               } else {
                   remoteResult
               }
            }
    }
}