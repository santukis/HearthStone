package com.santukis.datasources.local.daos

import androidx.room.Dao
import com.santukis.datasources.entities.dbo.DeckDBToCardDB

@Dao
interface DeckToCardDao: BaseDao<DeckDBToCardDB> {

}