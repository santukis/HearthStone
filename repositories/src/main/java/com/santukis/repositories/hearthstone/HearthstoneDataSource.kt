package com.santukis.repositories.hearthstone

import com.santukis.entities.hearthstone.*
import com.santukis.repositories.strategies.defaultError

interface HearthstoneDataSource {

    suspend fun getDeck(deckRequest: DeckRequest): Result<Deck> = defaultError()

    suspend fun saveDeck(deck: Deck): Result<Unit> = defaultError()

    suspend fun searchCards(searchCardsRequest: SearchCardsRequest): Result<List<Card>> = defaultError()

    suspend fun getCards(): Result<List<Card>> = defaultError()

    suspend fun saveCards(cards: List<Card>): Result<Unit> = defaultError()

    suspend fun getMetadata(regionality: Regionality): Result<Metadata> = defaultError()

    suspend fun saveMetadata(metadata: Metadata): Result<Unit> = defaultError()

    suspend fun setCardFavourite(card: Card): Result<Card> = defaultError()
}