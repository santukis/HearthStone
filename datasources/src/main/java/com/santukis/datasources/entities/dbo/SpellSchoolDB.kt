package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "spellSchools",
    primaryKeys = ["id"]
)
class SpellSchoolDB(
    @Embedded
    val identity: IdentityDB
    )