package com.santukis.repositories.hearthstone

import com.santukis.entities.hearthstone.Deck
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.repositories.strategies.defaultError

interface HearthstoneDataSource {

    suspend fun getDeck(deckRequest: DeckRequest): Result<Deck> = defaultError()

    suspend fun saveDeck(deck: Deck): Result<Deck> = defaultError()
}