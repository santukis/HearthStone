package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.ArenaDB

@Dao
interface ArenaDao: BaseDao<ArenaDB> {

    @Query("SELECT * FROM arenaIds")
    fun getArenaIds(): List<ArenaDB>

}