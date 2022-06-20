package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "cardSets",
    primaryKeys = ["id"]
)
data class CardSetDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    val type: String = "",

    val collectibleCount: Int = -1
)