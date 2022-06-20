package com.santukis.datasources.local.daos

import androidx.room.Dao
import com.santukis.datasources.entities.dbo.GameModeDB

@Dao
interface GameModeDao: BaseDao<GameModeDB> {

}