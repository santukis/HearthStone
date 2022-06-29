package com.santukis.datasources.remote

import kotlin.math.ceil

class PagingSource<Key> {

    private val pagingData: MutableMap<Key, PagingData> = mutableMapOf()

    fun shouldRequestMoreData(key: Key): Boolean =
        pagingData[key]?.shouldRequestMoreData() ?: true

    fun getNextPage(key: Key): Int? = pagingData[key]?.getNextPage()

    fun getPagingSize(key: Key): Int? = pagingData[key]?.pageSize

    fun updatePagingData(key: Key, data: PagingData) {
        pagingData[key] = data
    }
}

data class PagingData(
    val itemCount: Int,
    val pageCount: Int,
    val pageSize: Int,
    val currentPage: Int
) {

    fun shouldRequestMoreData(): Boolean =
        currentPage < pageCount
                || getNextPage() <= pageCount

    fun getNextPage(): Int =
        ceil((itemCount / pageSize.toFloat()) + 1).toInt()
}