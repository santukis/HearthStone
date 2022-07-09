package com.santukis.entities.paging

class PagingSource<Key>(private val pageSize: Int) {

    private val pagingData: MutableMap<Key, PagingData> = mutableMapOf()

    fun shouldRequestMoreData(key: Key): Boolean =
        pagingData[key]?.shouldRequestMoreData() ?: true

    fun getPagingData(key: Key): PagingData = pagingData[key] ?: getInitialPagingData()

    fun <Item> updatePagingData(key: Key, data: PagingResult<Item>) {
        getPagingData(key).let {
            pagingData[key] = PagingData(
                itemCount = data.itemCount,
                pageSize = pageSize,
                currentPage = it.currentPage + 1
            )

            println("$key -> ${pagingData[key]}")
        }
    }

    fun removePagingData(key: Key) {
        pagingData[key] = getInitialPagingData()
    }

    private fun getInitialPagingData() = PagingData(
        itemCount = 0,
        pageSize = pageSize,
        currentPage = 0
    )
}

data class PagingData(
    val itemCount: Int,
    val pageSize: Int,
    val currentPage: Int
) {

    private val pageCount = maxOf(itemCount / pageSize, 1)

    fun shouldRequestMoreData(): Boolean = currentPage < pageCount

}

data class PagingRequest<Item>(
    val shouldRefresh: Boolean = false,
    val request: Item
)

data class PagingResult<Item>(
    val itemCount: Int = Int.MAX_VALUE,
    val item: Item
)