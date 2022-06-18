package com.santukis.repositories.hearthstone

import com.santukis.entities.hearthstone.Deck
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.repositories.strategies.LocalRemoteStrategy
import com.santukis.usecases.hearthstone.GetDeckGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HearthstoneRepository(
    private val remoteHearthstoneDataSource: HearthstoneDataSource,
    private val localHearthstoneDataSource: HearthstoneDataSource
) : GetDeckGateway {

    override suspend fun getDeck(deckRequest: DeckRequest): Flow<Result<Deck>> {
        return flow {
            val response = object : LocalRemoteStrategy<DeckRequest, Deck>() {
                override suspend fun loadFromLocal(input: DeckRequest): Result<Deck> = localHearthstoneDataSource.getDeck(input)

                override suspend fun shouldUpdateFromRemote(input: DeckRequest, localOutput: Deck): Boolean = false

                override suspend fun loadFromRemote(input: DeckRequest): Result<Deck> = remoteHearthstoneDataSource.getDeck(input)

                override suspend fun saveIntoLocal(output: Deck): Result<Deck> = localHearthstoneDataSource.saveDeck(output)

            }.execute(deckRequest)

            emit(response)
        }
    }
}