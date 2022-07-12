package com.santukis.entities.paging


class PagingSource<Key>(private val pageSize: Int) {

    private val pagingData: MutableMap<Key, PagingData> = mutableMapOf()

    fun shouldRequestMoreData(key: Key): Boolean = !getPagingData(key).noMoreItems

    fun getPagingData(key: Key): PagingData = pagingData[key] ?: getInitialPagingData()

    fun updatePagingData(
        key: Key,
        itemCount: Int? = null,
        increaseNextPage: Boolean = false,
        noMoreItems: Boolean? = null
    ) {
        getPagingData(key).let { storedPagingData ->
            pagingData[key] = PagingData(
                pageSize = pageSize,
                itemCount = itemCount ?: storedPagingData.itemCount,
                nextPage = if (storedPagingData.nextPage == 1 && storedPagingData.itemCount > 0) {
                    (storedPagingData.itemCount / pageSize + 1)

                } else  {
                   if (increaseNextPage) storedPagingData.nextPage + 1 else storedPagingData.nextPage
                },
                noMoreItems = noMoreItems ?: storedPagingData.noMoreItems
            )
        }
    }

    fun removePagingData(key: Key) {
        pagingData[key] = getInitialPagingData()
    }

    private fun getInitialPagingData() = PagingData(
        pageSize = pageSize
    )
}

data class PagingData(
    val pageSize: Int,
    val itemCount: Int = 0,
    val nextPage: Int = 1,
    val noMoreItems: Boolean = false
)

data class PagingRequest<Item>(
    val shouldRefresh: Boolean = false,
    val itemCount: Int = 0,
    val request: Item
)

data class PagingResult<Item>(
    val increaseNextPage: Boolean = false,
    val noMoreItems: Boolean? = null,
    val item: Item
)