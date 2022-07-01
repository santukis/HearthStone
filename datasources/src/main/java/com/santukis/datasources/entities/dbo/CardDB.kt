package com.santukis.datasources.entities.dbo

import androidx.room.*
import com.santukis.entities.hearthstone.*

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

    val classId: Int = -1,

    val cardTypeId: Int = -1,

    val cardSetId: Int = -1,

    val rarityId: Int = -1,

    val spellSchoolId: Int = -1,

    val multiClassIds: List<Int> = emptyList(),

    val childIds: List<Int> = emptyList(),

    val parentId: Int = -1,

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
    ) val cardClass: CardClassDB? = null,

    @Relation(
        entity = CardTypeDB::class,
        parentColumn = "cardTypeId",
        entityColumn = "id"
    ) val cardType: CardTypeDetailDB? = null,

    @Relation(
        entity = CardSetDB::class,
        parentColumn = "cardSetId",
        entityColumn = "id"
    ) val cardSet: CardSetDB? = null,

    @Relation(
        entity = RarityDB::class,
        parentColumn = "rarityId",
        entityColumn = "id"
    ) val rarity: RarityDB? = null,

    @Relation(
        entity = SpellSchoolDB::class,
        parentColumn = "spellSchoolId",
        entityColumn = "id"
    ) val spellSchool: SpellSchoolDB? = null,

    @Relation(
        entity = KeywordDB::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CardDBToKeywordDB::class,
            parentColumn = "cardId",
            entityColumn = "keywordId"
        )
    ) val keywords: List<KeywordDetailDB> = emptyList(),

    @Relation(
        entity = FavouriteDB::class,
        parentColumn = "id",
        entityColumn = "cardId"
    ) val favouriteId: FavouriteDB? = null
) {

    fun toCard(): Card =
        Card(
            identity = card.identity.toIdentity(),
            collectible = Collectible.valueOf(card.collectible),
            cardClass = cardClass?.toCardClass() ?: CardClass(),
            multiClassIds = card.multiClassIds,
            cardType = cardType?.toCardType() ?: CardType(),
            cardSet = cardSet?.toCardSet() ?: CardSet(),
            rarity = rarity?.toRarity() ?: Rarity(),
            spellSchool = spellSchool?.toSpellSchool() ?: SpellSchool(),
            cardStats = card.cardStats.toCardStats(),
            cardText = card.cardText.toCardText(),
            images = card.image.toCardImage(),
            keywords = keywords.map { it.toKeyword() },
            childIds = card.childIds,
            isFavourite = favouriteId?.cardId == card.identity.id
        )
}
