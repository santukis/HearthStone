package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.santukis.datasources.entities.dbo.DeckDB
import com.santukis.datasources.entities.dbo.DeckDetailDB

@Dao
interface DeckDao: BaseDao<DeckDB> {

    @Transaction
    @Query("SELECT * FROM decks WHERE code = :code LIMIT 1")
    fun getDeck(code: String): DeckDetailDB?

}