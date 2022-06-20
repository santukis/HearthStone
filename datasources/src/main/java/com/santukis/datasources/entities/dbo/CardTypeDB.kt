package com.santukis.datasources.entities.dbo

import androidx.room.*

@Entity(
    tableName = "cardTypes",
    primaryKeys = ["id"]
)
data class CardTypeDB(
    @Embedded
    val identity: IdentityDB = IdentityDB()
)


@Entity(
    tableName = "cardTypesToGameModes",
    primaryKeys = ["cardTypeId", "gameModeId"],
    indices = [
        Index(value = ["gameModeId"], name = "GameModeId_For_CardType_index"),
        Index(value = ["cardTypeId", ], name = "CardTypeId_index")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CardTypeDB::class,
            parentColumns = ["id"],
            childColumns = ["cardTypeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GameModeDB::class,
            parentColumns = ["id"],
            childColumns = ["gameModeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CardTypeDBToGameModeDB(
    val cardTypeId: Int,
    val gameModeId: Int
)

data class CardTypeDetailDB(
    @Embedded
    val cardType: CardTypeDB,

    @Relation(
        entity = GameModeDB::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CardTypeDBToGameModeDB::class,
            parentColumn = "cardTypeId",
            entityColumn = "gameModeId"
        )
    ) val gameModes: List<GameModeDB> = emptyList()
)
