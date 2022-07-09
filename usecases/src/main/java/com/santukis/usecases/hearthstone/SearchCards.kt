package com.santukis.usecases.hearthstone

import com.santukis.entities.hearthstone.Card
import com.santukis.entities.hearthstone.SearchCardsRequest
import com.santukis.entities.paging.PagingRequest
import com.santukis.usecases.UseCase
import kotlinx.coroutines.flow.Flow

interface SearchCardsGateway {
    suspend fun searchCards(cardsRequest: PagingRequest<SearchCardsRequest>): Flow<Result<List<Card>>>
}

class SearchCards(private val gateway: SearchCardsGateway): UseCase<PagingRequest<SearchCardsRequest>, Flow<Result<List<Card>>>> {

    override suspend fun invoke(params: PagingRequest<SearchCardsRequest>): Flow<Result<List<Card>>> =
        gateway.searchCards(params)

}