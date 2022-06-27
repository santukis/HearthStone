package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity
import com.santukis.entities.hearthstone.SpellSchool

@Entity(
    tableName = "spellSchools",
    primaryKeys = ["id"]
)
class SpellSchoolDB(
    @Embedded
    val identity: IdentityDB
) {

    fun toSpellSchool(): SpellSchool =
        SpellSchool(
            identity = identity.toIdentity()
        )
}