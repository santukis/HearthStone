package com.santukis.datasources.local.daos

import androidx.room.Dao
import com.santukis.datasources.entities.dbo.CardDBToKeywordDB

@Dao
interface CardToKeywordDao: BaseDao<CardDBToKeywordDB> {

}