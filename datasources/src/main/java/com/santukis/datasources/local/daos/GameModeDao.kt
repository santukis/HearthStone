package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.GameModeDB

@Dao
interface GameModeDao: BaseDao<GameModeDB> {

    @Query("SELECT * FROM gameModes")
    fun getGameModes(): List<GameModeDB>
}