package com.santukis.usecases.hearthstone

import com.santukis.entities.hearthstone.Deck
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.usecases.UseCase
import kotlinx.coroutines.flow.Flow

interface GetDeckGateway {
    suspend fun getDeck(deckRequest: DeckRequest): Flow<Result<Deck>>
}

class GetDeck(private val gateway: GetDeckGateway): UseCase<DeckRequest, Flow<Result<Deck>>> {

    override suspend fun invoke(params: DeckRequest): Flow<Result<Deck>> =
        gateway.getDeck(params)

}