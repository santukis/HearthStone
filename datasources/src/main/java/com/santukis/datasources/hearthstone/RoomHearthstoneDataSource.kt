package com.santukis.datasources.hearthstone

import com.santukis.datasources.local.HearthstoneDatabase
import com.santukis.datasources.mappers.*
import com.santukis.entities.hearthstone.*
import com.santukis.repositories.hearthstone.HearthstoneDataSource

class RoomHearthstoneDataSource(private val database: HearthstoneDatabase): HearthstoneDataSource {

    override suspend fun saveMetadata(metadata: Metadata): Result<Unit> {
        database.cardSetDao().saveItems(metadata.sets.map { it.toCardSetDB() })
        database.gameModeDao().saveItems(metadata.gameModes.map { it.toGameModeDB() })
        database.arenaDao().saveItems(metadata.arenaIds.map { it.toArenaDB() })
        database.rarityDao().saveItems(metadata.rarities.map { it.toRarityDB() })
        database.cardClassDao().saveItems(metadata.classes.map { it.toCardClassDB() })
        database.spellSchoolDao().saveItems(metadata.spellSchools.map { it.toSpellSchoolDB() })

        saveCardTypes(metadata.types)
        saveMinionTypes(metadata.minionTypes)
        saveKeywords(metadata.keywords)

        return Result.success(Unit)
    }

    private fun saveCardTypes(cardTypes: List<CardType>) {
        cardTypes.forEach { cardType ->
            database.cardTypeDao().saveItem(cardType.toCardTypeDB())
            database.cardTypeToGameModeDao().saveItems(cardType.gameModes.toCardTypeToGameModeList(cardType.identity.id))
        }
    }

    private fun saveMinionTypes(minionTypes: List<MinionType>) {
        minionTypes.forEach { minionType ->
            database.minionTypeDao().saveItem(minionType.toMinionTypeDB())
            database.minionTypeToGameModeDao().saveItems(minionType.gameModes.toMinionTypeToGameModeList(minionType.identity.id))
        }
    }

    private fun saveKeywords(keywords: List<Keyword>) {
        keywords.forEach { keyword ->
            database.keywordDao().saveItem(keyword.toKeywordDB())
            database.keywordToGameModeDao().saveItems(keyword.gameModes.toKeywordToGameModeList(keyword.identity.id))
        }
    }

    override suspend fun getDeck(deckRequest: DeckRequest): Result<Deck> =
        database.deckDao().getDeck(deckRequest.deckCode)?.let { db ->
            Result.success(db.toDeck())
        } ?: Result.failure(Exception())

    override suspend fun saveDeck(deck: Deck): Result<Unit> {
        database.deckDao().saveItem(deck.toDeckDB())

        saveCards(listOf(deck.hero, deck.heroPower))
        saveCards(deck.cards)

        database.deckToCardDao().saveItems(deck.cards.toDeckToCardList(deck.code))
        database.cardClassDao().saveItem(deck.cardClass.toCardClassDB())

        return Result.success(Unit)
    }

    override suspend fun searchCards(searchCardsRequest: SearchCardsRequest): Result<List<Card>> {
        val cards = database.cardDao().searchCards(searchCardsRequest.toSqliteQuery()).map { it.toCard() }

        return when {
            cards.isNotEmpty() -> Result.success(cards)
            else -> Result.failure(Exception("No items stored in database"))
        }
    }

    override suspend fun saveCards(cards: List<Card>): Result<Unit> {
        cards.forEach { card ->
            database.cardDao().saveItem(card.toCardDB())
            database.cardToKeywordDao().saveItems(card.keywords.toCardToKeywordList(card.identity.id))
        }

        return Result.success(Unit)
    }

    override suspend fun getCards(): Result<List<Card>> {
        val cards = database.cardDao().getCards().map { it.toCard() }

        return when {
            cards.isNotEmpty() -> Result.success(cards)
            else -> Result.failure(Exception("No items stored in database"))
        }
    }
}