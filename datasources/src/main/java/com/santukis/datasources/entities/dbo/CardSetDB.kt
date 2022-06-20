package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity
import com.santukis.entities.hearthstone.CardSet
import com.santukis.entities.hearthstone.SetType

@Entity(
    tableName = "cardSets",
    primaryKeys = ["id"]
)
data class CardSetDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    val type: String = "",

    val collectibleCount: Int = -1
) {

    fun toCardSet(): CardSet =
        CardSet(
            identity = identity.toIdentity(),
            type = SetType.valueOf(type),
            collectibleCount = collectibleCount
        )
}