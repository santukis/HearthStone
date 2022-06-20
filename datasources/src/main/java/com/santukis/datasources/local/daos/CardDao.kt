package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.santukis.datasources.entities.dbo.CardDB
import com.santukis.datasources.entities.dbo.CardDetailDB

@Dao
interface CardDao: BaseDao<CardDB> {

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    fun getCard(cardId: Int): CardDetailDB?

}