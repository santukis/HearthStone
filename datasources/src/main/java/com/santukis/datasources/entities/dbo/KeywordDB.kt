package com.santukis.datasources.entities.dbo

import androidx.room.*
import com.santukis.entities.hearthstone.Keyword

@Entity(
    tableName = "keywords",
    primaryKeys = ["id"]
)
data class KeywordDB(
    @Embedded
    val identity: IdentityDB = IdentityDB(),

    @Embedded
    val cardText: CardTextDB = CardTextDB()
)

@Entity(
    tableName = "keywordsToGameModes",
    primaryKeys = ["keywordId", "gameModeId"],
    indices = [
        Index(value = ["keywordId"], name = "KeywordId_For_GameModes_index"),
        Index(value = ["gameModeId", ], name = "GameModeId_For_Keywords_index")
    ],
    foreignKeys = [
        ForeignKey(
            entity = KeywordDB::class,
            parentColumns = ["id"],
            childColumns = ["keywordId"],
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
data class KeywordDBToGameModeDB(
    val keywordId: Int,
    val gameModeId: Int
)

data class KeywordDetailDB(
    @Embedded
    val keyword: KeywordDB,

    @Relation(
        entity = GameModeDB::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = KeywordDBToGameModeDB::class,
            parentColumn = "keywordId",
            entityColumn = "gameModeId"
        )
    ) val gameModes: List<GameModeDB> = emptyList()
) {

    fun toKeyword(): Keyword =
        Keyword(
            identity = keyword.identity.toIdentity(),
            cardText = keyword.cardText.toCardText(),
            gameModes = gameModes.map { it.toGameMode() }
        )
}
