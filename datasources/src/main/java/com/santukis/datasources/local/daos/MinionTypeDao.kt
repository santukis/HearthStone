package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.santukis.datasources.entities.dbo.MinionTypeDB
import com.santukis.datasources.entities.dbo.MinionTypeDetailDB

@Dao
interface MinionTypeDao: BaseDao<MinionTypeDB> {

    @Transaction
    @Query("SELECT * FROM minionTypes")
    fun getMinionTypes(): List<MinionTypeDetailDB>
}