package com.santukis.datasources.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.santukis.datasources.entities.dbo.SpellSchoolDB

@Dao
interface SpellSchoolDao: BaseDao<SpellSchoolDB> {

    @Query("SELECT * FROM spellSchools")
    fun getSpellSchools(): List<SpellSchoolDB>
}