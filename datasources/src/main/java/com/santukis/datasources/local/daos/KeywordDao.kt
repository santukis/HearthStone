package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.KeywordDB
import com.santukis.datasources.entities.dbo.KeywordDetailDB

@Dao
interface KeywordDao: BaseDao<KeywordDB> {

    @Query("SELECT * FROM keywords")
    fun getKeywords(): List<KeywordDetailDB>
}