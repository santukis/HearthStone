package com.santukis.repositories.strategies

import com.santukis.entities.paging.PagingData
import com.santukis.entities.paging.PagingRequest
import com.santukis.entities.paging.PagingResult
import com.santukis.entities.paging.PagingSource

abstract class LocalRemotePagingStrategy<Input, Output>(
    private val pagingSource: PagingSource<String>
) : RepositoryStrategy<PagingRequest<Input>, Output> {

    companion object {
        private const val MIN_STORED_DATA = 10
    }

    protected abstract suspend fun loadFromLocal(input: Input, pagingData: PagingData): Result<PagingResult<Output>>

    protected abstract suspend fun loadFromRemote(
        input: Input, pagingData: PagingData
    ): Result<PagingResult<Output>>

    protected abstract suspend fun saveIntoLocal(output: Output)

    override suspend fun execute(input: PagingRequest<Input>): Result<Output> {
        val key = input.request.hashCode().toString()

        if (input.shouldRefresh) {
            pagingSource.removePagingData(key)

        } else {
            pagingSource.updatePagingData(
                key = key,
                itemCount = input.itemCount
            )
        }

        var result: Result<PagingResult<Output>> = noMoreData()


        if (pagingSource.shouldRequestMoreData(key)) {
            result = loadFromLocal(
                input = input.request,
                pagingData = pagingSource.getPagingData(key)
            )

            if (result.isFailure || pagingSource.getPagingData(key).itemCount < MIN_STORED_DATA) {
                loadFromRemote(input.request, pagingSource.getPagingData(key))
                    .onSuccess {
                        pagingSource.updatePagingData(
                            key = key,
                            increaseNextPage = it.increaseNextPage,
                            noMoreItems = it.noMoreItems
                        )

                        saveIntoLocal(it.item)
                    }
                    .onFailure {
                        pagingSource.updatePagingData(
                            key = key,
                            noMoreItems = true
                        )
                    }

                result = loadFromLocal(
                    input = input.request,
                    pagingData = pagingSource.getPagingData(key)
                )
            }
        }

        return result.map { it.item }
    }

}