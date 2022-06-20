package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "rarities",
    primaryKeys = ["id"]
)
data class RarityDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    val craftingCost: List<Int> = emptyList(),

    val dustValue: List<Int> = emptyList()
)