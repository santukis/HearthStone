package com.santukis.datasources.entities.dbo

import androidx.room.*
import com.santukis.entities.hearthstone.MinionType

@Entity(
    tableName = "minionTypes",
    primaryKeys = ["id"]
)
class MinionTypeDB(
    @Embedded
    val identity: IdentityDB = IdentityDB()
)

@Entity(
    tableName = "minionTypesToGameModes",
    primaryKeys = ["minionTypeId", "gameModeId"],
    indices = [
        Index(value = ["gameModeId"], name = "GameModeId_For_MinionTypes_index"),
        Index(value = ["minionTypeId", ], name = "MinionTypeId_index")
    ],
    foreignKeys = [
        ForeignKey(
            entity = MinionTypeDB::class,
            parentColumns = ["id"],
            childColumns = ["minionTypeId"],
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
data class MinionTypeDBToGameModeDB(
    val minionTypeId: Int,
    val gameModeId: Int
)

data class MinionTypeDetailDB(
    @Embedded
    val minionType: MinionTypeDB,

    @Relation(
        entity = GameModeDB::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MinionTypeDBToGameModeDB::class,
            parentColumn = "minionTypeId",
            entityColumn = "gameModeId"
        )
    ) val gameModes: List<GameModeDB> = emptyList()
) {

    fun toMinionType(): MinionType =
        MinionType(
            identity = minionType.identity.toIdentity(),
            gameModes = gameModes.map { it.toGameMode() }
        )
}