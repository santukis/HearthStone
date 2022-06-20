package com.santukis.datasources.entities.dbo

import androidx.room.*

@Entity(
    tableName = "cards",
    primaryKeys = ["id"]
)
data class CardDB(
    @Embedded
    val identity: IdentityDB,

    val collectible: String,

    @Embedded
    val cardText: CardTextDB,

    @Embedded
    val cardStats: CardStatsDB,

    @Embedded
    val image: CardImageDB,

    val classId: Int? = null,

    val cardTypeId: Int? = null,

    val cardSetId: Int? = null,

    val rarityId: Int? = null,

    val multiClassIds: List<Int>? = null,

    val childIds: List<Int>? = null,

    val parentId: Int? = null,

    val updatedAt: Long = 0
)

@Entity(
    tableName = "cardsToKeywords",
    primaryKeys = ["cardId", "keywordId"],
    indices = [
        Index(value = ["cardId"], name = "CardId_For_Keywords_index"),
        Index(value = ["keywordId", ], name = "KeywordId_For_Cards_index")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CardDB::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = KeywordDB::class,
            parentColumns = ["id"],
            childColumns = ["keywordId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CardDBToKeywordDB(
    val cardId: Int,
    val keywordId: Int
)

data class CardDetailDB(
    @Embedded
    val card: CardDB,

    @Relation(
        entity = CardClassDB::class,
        parentColumn = "classId",
        entityColumn = "id"
    ) val cardClass: CardClassDB?,

    @Relation(
        entity = CardTypeDB::class,
        parentColumn = "cardTypeId",
        entityColumn = "id"
    ) val cardType: CardTypeDetailDB?,

    @Relation(
        entity = CardSetDB::class,
        parentColumn = "cardSetId",
        entityColumn = "id"
    ) val cardSet: CardSetDB?,

    @Relation(
        entity = RarityDB::class,
        parentColumn = "rarityId",
        entityColumn = "id"
    ) val rarity: RarityDB?,

    @Relation(
        entity = KeywordDB::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CardDBToKeywordDB::class,
            parentColumn = "cardId",
            entityColumn = "keywordId"
        )
    ) val keywords: List<KeywordDetailDB> = emptyList()
)
