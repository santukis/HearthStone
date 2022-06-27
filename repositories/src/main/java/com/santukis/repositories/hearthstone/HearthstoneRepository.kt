package com.santukis.repositories.hearthstone

import com.santukis.entities.hearthstone.*
import com.santukis.repositories.strategies.LocalRemoteStrategy
import com.santukis.usecases.core.emitIfSuccess
import com.santukis.usecases.hearthstone.GetDeckGateway
import com.santukis.usecases.hearthstone.LoadMetadataGateway
import com.santukis.usecases.hearthstone.SearchCardsGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class HearthstoneRepository(
    private val remoteHearthstoneDataSource: HearthstoneDataSource,
    private val localHearthstoneDataSource: HearthstoneDataSource
) :
    GetDeckGateway,
    LoadMetadataGateway,
    SearchCardsGateway {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadMetadata(Regionality.Europe(EuropeLocale.Spanish())).single()
        }
    }

    override suspend fun loadMetadata(regionality: Regionality): Flow<Result<Unit>> {
        return flow {
            val response = object : LocalRemoteStrategy<Regionality, Metadata>() {
                override suspend fun shouldLoadFromLocal(input: Regionality): Boolean = true

                override suspend fun loadFromLocal(input: Regionality): Result<Metadata> = localHearthstoneDataSource.getMetadata(input)

                override suspend fun loadFromRemote(input: Regionality, localResult: Metadata?): Result<Metadata> = remoteHearthstoneDataSource.getMetadata(input)

                override suspend fun shouldUpdateFromRemote(input: Regionality, localOutput: Metadata): Boolean = false

                override suspend fun saveIntoLocal(output: Metadata) {
                    localHearthstoneDataSource.saveMetadata(output)
                }

            }.execute(regionality)

            emit(response.map { })
        }
    }

    override suspend fun getDeck(deckRequest: DeckRequest): Flow<Result<Deck>> {
        return flow {
            val response = object : LocalRemoteStrategy<DeckRequest, Deck>() {
                override suspend fun shouldLoadFromLocal(input: DeckRequest): Boolean = true

                override suspend fun loadFromLocal(input: DeckRequest): Result<Deck> = localHearthstoneDataSource.getDeck(input)

                override suspend fun shouldUpdateFromRemote(input: DeckRequest, localOutput: Deck): Boolean = false

                override suspend fun loadFromRemote(input: DeckRequest, localResult: Deck?): Result<Deck> = remoteHearthstoneDataSource.getDeck(input)

                override suspend fun saveIntoLocal(output: Deck) {
                    localHearthstoneDataSource.saveDeck(output)
                }

            }.execute(deckRequest)

            emit(response)
        }
    }

    override suspend fun searchCards(cardsRequest: SearchCardsRequest): Flow<Result<List<Card>>> {
        return flow {
            val response = object : LocalRemoteStrategy<SearchCardsRequest, List<Card>>() {
                override suspend fun shouldLoadFromLocal(input: SearchCardsRequest): Boolean =
                    input.itemCount == 0

                override suspend fun loadFromLocal(input: SearchCardsRequest): Result<List<Card>> {
                    val localResult = localHearthstoneDataSource.searchCards(input)
                    localResult.emitIfSuccess(this@flow)
                    return localResult
                }

                override suspend fun shouldUpdateFromRemote(input: SearchCardsRequest, localOutput: List<Card>): Boolean =
                    true

                override suspend fun loadFromRemote(input: SearchCardsRequest, localResult: List<Card>?): Result<List<Card>> =
                    remoteHearthstoneDataSource.searchCards(input.copy(itemCount = localResult?.size ?: 0))

                override suspend fun saveIntoLocal(output: List<Card>) {
                    localHearthstoneDataSource.saveCards(output)
                }

            }.execute(cardsRequest)

            emit(response)
        }
    }
}