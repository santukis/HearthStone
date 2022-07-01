package com.santukis.usecases.hearthstone

import com.santukis.entities.hearthstone.Card
import com.santukis.usecases.UseCase

interface UpdateCardFavouriteGateway {
    suspend fun setCardFavourite(card: Card): Result<Card>
}

class UpdateCardFavourite(private val gateway: UpdateCardFavouriteGateway): UseCase<Card, Result<Card>> {

    override suspend fun invoke(params: Card): Result<Card> =
        gateway.setCardFavourite(params)
}