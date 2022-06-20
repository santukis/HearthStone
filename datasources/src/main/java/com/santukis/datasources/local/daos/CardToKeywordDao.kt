package com.santukis.datasources.local.daos

import androidx.room.Dao
import com.santukis.datasources.entities.dbo.CardDBToKeywordDB
import com.santukis.datasources.entities.dbo.KeywordDBToGameModeDB

@Dao
interface CardToKeywordDao: BaseDao<CardDBToKeywordDB> {

}