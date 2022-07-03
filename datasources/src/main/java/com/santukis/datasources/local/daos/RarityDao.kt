package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.RarityDB

@Dao
interface RarityDao: BaseDao<RarityDB> {

    @Query("SELECT * FROM rarities")
    fun getRarities(): List<RarityDB>
}