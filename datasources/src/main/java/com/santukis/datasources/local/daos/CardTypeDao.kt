package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.CardTypeDB
import com.santukis.datasources.entities.dbo.CardTypeDetailDB

@Dao
interface CardTypeDao: BaseDao<CardTypeDB> {

    @Query("SELECT * FROM cardTypes")
    fun getCardTypes(): List<CardTypeDetailDB>
}