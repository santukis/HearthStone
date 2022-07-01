package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.santukis.datasources.entities.dbo.FavouriteDB

@Dao
interface FavouriteDao: BaseDao<FavouriteDB> {

    @Query("SELECT * FROM favourites WHERE cardId = :cardId LIMIT 1")
    fun isFavourite(cardId: Int): FavouriteDB?

    @Transaction
    fun updateCardFavourite(cardId: Int): Boolean {
        val response = when (val isFavourite = isFavourite(cardId)) {
            null -> saveItem(FavouriteDB(cardId)).toInt()
            else -> deleteItem(isFavourite)
        }

        return response > 0
    }
}