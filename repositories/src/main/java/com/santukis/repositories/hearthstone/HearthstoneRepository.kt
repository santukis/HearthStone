package com.santukis.repositories.hearthstone

import com.santukis.entities.hearthstone.*
import com.santukis.entities.paging.PagingData
import com.santukis.entities.paging.PagingRequest
import com.santukis.entities.paging.PagingResult
import com.santukis.entities.paging.PagingSource
import com.santukis.repositories.strategies.LocalRemotePagingStrategy
import com.santukis.repositories.strategies.LocalRemoteStrategy
import com.santukis.repositories.strategies.LocalStrategy
import com.santukis.usecases.hearthstone.GetDeckGateway
import com.santukis.usecases.hearthstone.LoadMetadataGateway
import com.santukis.usecases.hearthstone.SearchCardsGateway
import com.santukis.usecases.hearthstone.UpdateCardFavouriteGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HearthstoneRepository(
    private val remoteHearthstoneDataSource: HearthstoneDataSource,
    private val localHearthstoneDataSource: HearthstoneDataSource
) :
    GetDeckGateway,
    LoadMetadataGateway,
    SearchCardsGateway,
    UpdateCardFavouriteGateway {

    companion object {
        private const val PAGE_SIZE = 25
    }

    private val pagingSource: PagingSource<String> = PagingSource(pageSize = PAGE_SIZE)

    override suspend fun loadMetadata(regionality: Regionality): Flow<Result<Metadata>> {
        return flow {
            val response = object : LocalRemoteStrategy<Regionality, Metadata>() {
                override suspend fun shouldLoadFromLocal(input: Regionality): Boolean = true

                override suspend fun loadFromLocal(input: Regionality): Result<Metadata> =
                    localHearthstoneDataSource.getMetadata(input)

                override suspend fun loadFromRemote(
                    input: Regionality,
                    localResult: Metadata?
                ): Result<Metadata> = remoteHearthstoneDataSource.getMetadata(input)

                override suspend fun shouldUpdateFromRemote(
                    input: Regionality,
                    localOutput: Metadata
                ): Boolean = false

                override suspend fun saveIntoLocal(output: Metadata) {
                    localHearthstoneDataSource.saveMetadata(output)
                }

            }.execute(regionality)

            emit(response)
        }
    }

    override suspend fun getDeck(deckRequest: DeckRequest): Flow<Result<Deck>> {
        return flow {
            val response = object : LocalRemoteStrategy<DeckRequest, Deck>() {
                override suspend fun shouldLoadFromLocal(input: DeckRequest): Boolean = true

                override suspend fun loadFromLocal(input: DeckRequest): Result<Deck> =
                    localHearthstoneDataSource.getDeck(input)

                override suspend fun shouldUpdateFromRemote(
                    input: DeckRequest,
                    localOutput: Deck
                ): Boolean = false

                override suspend fun loadFromRemote(
                    input: DeckRequest,
                    localResult: Deck?
                ): Result<Deck> = remoteHearthstoneDataSource.getDeck(input)

                override suspend fun saveIntoLocal(output: Deck) {
                    localHearthstoneDataSource.saveDeck(output)
                }

            }.execute(deckRequest)

            emit(response)
        }
    }

    override suspend fun searchCards(cardsRequest: PagingRequest<SearchCardsRequest>): Flow<Result<List<Card>>> {
        return flow {
            val response = object : LocalRemotePagingStrategy<SearchCardsRequest, List<Card>>(pagingSource) {
                override suspend fun loadFromLocal(
                    input: SearchCardsRequest,
                    pagingData: PagingData
                ): Result<PagingResult<List<Card>>> =
                    localHearthstoneDataSource.searchCards(input, pagingData)

                override suspend fun loadFromRemote(
                    input: SearchCardsRequest,
                    pagingData: PagingData
                ): Result<PagingResult<List<Card>>> = remoteHearthstoneDataSource.searchCards(input, pagingData)

                override suspend fun saveIntoLocal(output: List<Card>) {
                    localHearthstoneDataSource.saveCards(output)
                }

            }.execute(cardsRequest)

            emit(response)
        }
    }

    override suspend fun setCardFavourite(card: Card): Result<Card> {
        return object : LocalStrategy<Card, Card>() {
            override suspend fun loadFromLocal(input: Card): Result<Card> {
                return localHearthstoneDataSource.setCardFavourite(card)
            }
        }.execute(card)
    }
}