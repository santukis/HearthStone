package com.santukis.datasources.hearthstone

import com.santukis.datasources.entities.dto.DeckResponseDTO
import com.santukis.datasources.entities.dto.HearthstoneErrorDTO
import com.santukis.datasources.entities.dto.requests.DeckRequestDTO
import com.santukis.datasources.mappers.toDeckRequestDTO
import com.santukis.datasources.remote.HttpClient
import com.santukis.datasources.remote.unwrapCall
import com.santukis.entities.hearthstone.Deck
import com.santukis.entities.hearthstone.DeckRequest
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
            onError = { Exception(it.error?.message) }
        )
    }
}