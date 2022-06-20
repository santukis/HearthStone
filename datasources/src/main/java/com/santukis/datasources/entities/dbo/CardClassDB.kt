package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity
import com.santukis.entities.hearthstone.CardClass

@Entity(
    tableName = "cardClass",
    primaryKeys = ["id"]
)
data class CardClassDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    val cardId: Int = -1,

    val heroPowerCardId: Int = -1,

    val alternativeHeroCardIds: List<Int> = emptyList()
) {

    fun toCardClass(): CardClass =
        CardClass(
            identity = identity.toIdentity(),
            cardId = cardId,
            heroPowerCardId = heroPowerCardId,
            alternativeHeroCardIds = alternativeHeroCardIds
        )
}