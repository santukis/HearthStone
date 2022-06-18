package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "cards",
    primaryKeys = ["id"]
)
data class CardDB(
    @Embedded
    val identity: IdentityDB,

    val collectible: String,

    @Embedded
    val image: CardImageDB,

    val classId: Int? = null,

    val cardTypeId: Int? = null,

    val cardSetId: Int? = null,

    val rarityId: Int? = null,

    val keywordIds: List<Int>? = null,

    val multiClassIds: List<Int>? = null,

    val childIds: List<Int>? = null,

    val parentId: Int? = null,

    @Embedded
    val cardText: CardTextDB,

    @Embedded
    val cardStats: CardStatsDB,

    val updatedAt: Long = 0
)
