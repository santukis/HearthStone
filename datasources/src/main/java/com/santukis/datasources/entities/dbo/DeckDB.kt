package com.santukis.datasources.entities.dbo

import androidx.room.*
import com.santukis.datasources.mappers.orDefault
import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.Deck

@Entity(
    tableName = "decks"
)
data class DeckDB(
    @PrimaryKey
    val code: String = "",

    val version: Int = 0,

    val format: String = "",

    val heroId: Int? = null,

    val heroPowerId: Int? = null,

    val classId: Int? = null,

    val cardCount: Int = 0
)

@Entity(
    tableName = "deckToCards",
    primaryKeys = ["deckId", "cardId"],
    indices = [
        Index(value = ["cardId"], name = "CardId_For_Decks_index"),
        Index(value = ["deckId", ], name = "DeckId_For_Cards_index")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CardDB::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeckDB::class,
            parentColumns = ["code"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DeckDBToCardDB(
    val deckId: String,
    val cardId: Int
)

data class DeckDetailDB(
    @Embedded
    val deck: DeckDB,

    @Relation(
        entity = CardDB::class,
        parentColumn = "heroId",
        entityColumn = "id"
    ) val hero: CardDetailDB?,

    @Relation(
        entity = CardDB::class,
        parentColumn = "heroPowerId",
        entityColumn = "id"
    ) val heroPower: CardDetailDB?,

    @Relation(
        entity = CardClassDB::class,
        parentColumn = "classId",
        entityColumn = "id"
    ) val cardClass: CardClassDB?,

    @Relation(
        entity = CardDB::class,
        parentColumn = "code",
        entityColumn = "id",
        associateBy = Junction(
            value = DeckDBToCardDB::class,
            parentColumn = "deckId",
            entityColumn = "cardId"
        )
    ) val cards: List<CardDetailDB> = emptyList()
) {

    fun toDeck(): Deck =
        Deck(
            code = deck.code,
            version = deck.version,
            format = deck.format,
            hero = hero?.toCard().orDefault(),
            heroPower = heroPower?.toCard().orDefault(),
            cardClass = cardClass?.toCardClass().orDefault(),
            cards = cards.map { it.toCard() },
            cardCount = deck.cardCount
        )
}