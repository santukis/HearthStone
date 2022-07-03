package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.CardClassDB

@Dao
interface CardClassDao: BaseDao<CardClassDB> {

    @Query("SELECT * FROM cardclass")
    fun getCardClasses(): List<CardClassDB>
}