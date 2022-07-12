package com.santukis.datasources.local.daos

import androidx.room.*

@Dao
interface BaseDao<Item> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveItem(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveItems(items: List<Item>): List<Long>

    @Delete
    fun deleteItem(item: Item): Int

    @Update
    fun updateItem(item: Item): Int
}