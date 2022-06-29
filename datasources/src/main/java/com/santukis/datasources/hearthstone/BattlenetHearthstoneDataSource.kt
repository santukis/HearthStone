package com.santukis.datasources.hearthstone

import com.santukis.datasources.entities.dto.CardsResponse
import com.santukis.datasources.entities.dto.DeckResponse
import com.santukis.datasources.entities.dto.HearthstoneErrorDTO
import com.santukis.datasources.entities.dto.MetadataResponse
import com.santukis.datasources.entities.dto.requests.DeckRequestDTO
import com.santukis.datasources.mappers.toDeckRequestDTO
import com.santukis.datasources.mappers.toPagingKey
import com.santukis.datasources.mappers.toSearchCardsRequestDTO
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.PagingSource
import com.santukis.datasources.remote.unwrapCall
import com.santukis.entities.hearthstone.*
import com.santukis.repositories.hearthstone.HearthstoneDataSource

class BattlenetHearthstoneDataSource(private val client: HttpClient) : HearthstoneDataSource {

    companion object {
        private const val SEARCH_PAGE_SIZE = 25
    }

    private val pagingSource: PagingSource<String> = PagingSource()

    override suspend fun getDeck(deckRequest: DeckRequest): Result<Deck> {
        val deckDTO: DeckRequestDTO = deckRequest.toDeckRequestDTO()
        val deckEndpoint = client.environment.deckEndpoint(deckRequest.regionality.region)

        return client.hearthstoneService.getDeck(
            baseUrl = deckEndpoint,
            locale = deckDTO.locale,
            code = deckDTO.deckCode,
            ids = deckDTO.cardIds,
            heroId = deckDTO.heroId

        ).unwrapCall<HearthstoneErrorDTO, DeckResponse, Deck>(
            onSuccess = { it.toDeck() },
            onError = { it.toException() }
        )
    }

    override suspend fun searchCards(searchCardsRequest: SearchCardsRequest): Result<List<Card>> {
        val searchCardsRequestDTO = searchCardsRequest.toSearchCardsRequestDTO()
        val searchEndpoint = client.environment.searchCards(searchCardsRequest.regionality.region)
        val pagingKey = searchCardsRequest.toPagingKey(searchEndpoint)

        return if (pagingSource.shouldRequestMoreData(pagingKey)) {

            client.hearthstoneService.searchCards(
                baseUrl = searchEndpoint,
                locale = searchCardsRequestDTO.locale,
                set = searchCardsRequestDTO.set,
                classSlug = searchCardsRequestDTO.classSlug,
                manaCost = searchCardsRequestDTO.manaCost,
                attack = searchCardsRequestDTO.attack,
                health = searchCardsRequestDTO.health,
                collectible = searchCardsRequestDTO.collectible,
                rarity = searchCardsRequestDTO.rarity,
                type = searchCardsRequestDTO.type,
                minionType = searchCardsRequestDTO.minionType,
                keyword = searchCardsRequestDTO.keyword,
                textFilter = searchCardsRequestDTO.textFilter,
                gameMode = searchCardsRequestDTO.gameMode,
                spellSchool = searchCardsRequestDTO.spellSchool,
                page = pagingSource.getNextPage(searchEndpoint),
                pageSize = pagingSource.getPagingSize(searchEndpoint),
                sort = searchCardsRequestDTO.sort

            ).unwrapCall<HearthstoneErrorDTO, CardsResponse, List<Card>>(
                onSuccess = { cardResponse ->
                    pagingSource.updatePagingData(
                        pagingKey,
                        cardResponse.toPagingData(SEARCH_PAGE_SIZE)
                    )

                    cardResponse.toCardList()
                },
                onError = {
                    it.toException()
                }
            )

        } else {
            Result.failure(Exception("No more data"))
        }
    }

    override suspend fun getMetadata(regionality: Regionality): Result<Metadata> {
        val metadataEndpoint = client.environment.metadata(regionality.region)

        return client.hearthstoneService.getMetadata(
            baseUrl = metadataEndpoint,
            locale = regionality.locale.value

        ).unwrapCall<HearthstoneErrorDTO, MetadataResponse, Metadata>(
            onSuccess = { it.toMetadata() },
            onError = { it.toException() }
        )
    }
}