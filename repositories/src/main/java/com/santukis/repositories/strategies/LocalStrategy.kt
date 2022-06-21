package com.santukis.repositories.strategies

abstract class LocalStrategy<Input, Output>: RepositoryStrategy<Input, Output> {

    protected abstract suspend fun loadFromLocal(input: Input): Result<Output>

    override suspend fun execute(input: Input): Result<Output> = loadFromLocal(input)
}