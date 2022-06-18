package com.santukis.datasources.mappers

import com.santukis.datasources.entities.dto.requests.DeckRequestDTO
import com.santukis.entities.hearthstone.DeckRequest

fun DeckRequest.toDeckRequestDTO(): DeckRequestDTO =
    DeckRequestDTO(
        locale = regionality.locale.value,
        deckCode = deckCode.takeIfNotEmpty(),
        cardIds = cardIds.joinToString(","),
        heroId = heroId.takeIfNotEmpty()
    )