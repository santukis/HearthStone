package com.santukis.datasources.hearthstone

import com.santukis.datasources.entities.dto.CardsResponse
import com.santukis.datasources.entities.dto.DeckResponseDTO
import com.santukis.datasources.entities.dto.HearthstoneErrorDTO
import com.santukis.datasources.entities.dto.requests.DeckRequestDTO
import com.santukis.datasources.mappers.toDeckRequestDTO
import com.santukis.datasources.mappers.toSearchCardsRequestDTO
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.unwrapCall
import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.Deck
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.entities.hearthstone.SearchCardsRequest
import com.santukis.repositories.hearthstone.HearthstoneDataSource

class BattlenetHearthstoneDataSource(private val client: HttpClient) : HearthstoneDataSource {

    override suspend fun getDeck(deckRequest: DeckRequest): Result<Deck> {
        val deckDTO: DeckRequestDTO = deckRequest.toDeckRequestDTO()
        val deckEndpoint = client.environment.deckEndpoint(deckRequest.regionality.region)

        return client.hearthstoneService.getDeck(
            baseUrl = deckEndpoint,
            locale = deckDTO.locale,
            code = deckDTO.deckCode,
            ids = deckDTO.cardIds,
            heroId = deckDTO.heroId

        ).unwrapCall<HearthstoneErrorDTO, DeckResponseDTO, Deck>(
            onSuccess = { it.toDeck() },
            onError = { it.toException() }
        )
    }

    override suspend fun searchCards(searchCardsRequest: SearchCardsRequest): Result<List<Card>> {
        val searchCardsRequestDTO = searchCardsRequest.toSearchCardsRequestDTO()
        val searchEndpoint = client.environment.searchCards(searchCardsRequest.regionality.region)

        return client.hearthstoneService.searchCards(
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
            page = searchCardsRequestDTO.page,
            pageSize = searchCardsRequestDTO.pageSize,
            sort = searchCardsRequestDTO.sort

        ).unwrapCall<HearthstoneErrorDTO, CardsResponse, List<Card>>(
            onSuccess = { it.toCardList() },
            onError = { it.toException() }
        )
    }
}