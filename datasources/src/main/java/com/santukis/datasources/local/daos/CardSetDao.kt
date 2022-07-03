package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.CardSetDB

@Dao
interface CardSetDao: BaseDao<CardSetDB> {

    @Query("SELECT * FROM cardSets")
    fun getCardSets(): List<CardSetDB>
}