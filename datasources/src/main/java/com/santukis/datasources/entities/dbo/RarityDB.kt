package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity
import com.santukis.entities.hearthstone.Rarity

@Entity(
    tableName = "rarities",
    primaryKeys = ["id"]
)
data class RarityDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    val craftingCost: List<Int> = emptyList(),

    val dustValue: List<Int> = emptyList()
) {

    fun toRarity(): Rarity =
        Rarity(
            identity = identity.toIdentity(),
            craftingCost = craftingCost,
            dustValue = dustValue
        )
}