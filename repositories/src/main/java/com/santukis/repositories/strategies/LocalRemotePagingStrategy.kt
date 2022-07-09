package com.santukis.repositories.strategies

import com.santukis.entities.paging.PagingData
import com.santukis.entities.paging.PagingRequest
import com.santukis.entities.paging.PagingResult
import com.santukis.entities.paging.PagingSource

abstract class LocalRemotePagingStrategy<Input, Output>(
    private val pagingSource: PagingSource<String>
) : RepositoryStrategy<PagingRequest<Input>, Output> {

    protected abstract suspend fun loadFromLocal(input: Input, pagingData: PagingData): Result<PagingResult<Output>>

    protected abstract suspend fun loadFromRemote(
        input: Input, pagingData: PagingData
    ): Result<PagingResult<Output>>

    protected abstract suspend fun saveIntoLocal(output: Output)

    override suspend fun execute(input: PagingRequest<Input>): Result<Output> {
        val localKey = "local_${input.hashCode()}"
        val remoteKey = "remote_${input.hashCode()}"

        var result: Result<PagingResult<Output>> = noMoreData()

        if (input.shouldRefresh) {
            pagingSource.removePagingData(localKey)
            pagingSource.removePagingData(remoteKey)
        }

        if (pagingSource.shouldRequestMoreData(localKey)) {
            result = loadFromLocal(input.request, pagingSource.getPagingData(localKey))

            if (result.isSuccess) {
                val pagingResult = result.getOrThrow()
                pagingSource.updatePagingData(localKey, pagingResult)
                pagingSource.updatePagingData(remoteKey, pagingResult.copy(itemCount = Int.MAX_VALUE))
            }
        }

        if (result.isFailure && pagingSource.shouldRequestMoreData(remoteKey)) {
            result = loadFromRemote(input.request, pagingSource.getPagingData(remoteKey))
                .fold(
                    onSuccess = {
                        pagingSource.updatePagingData(remoteKey, it)

                        saveIntoLocal(it.item)
                        loadFromLocal(input.request, pagingSource.getPagingData(localKey))
                    },
                    onFailure = {
                        Result.failure(it)
                    }
                )
        }

        return result.map { it.item }
    }
}