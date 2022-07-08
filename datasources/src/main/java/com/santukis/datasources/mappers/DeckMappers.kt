package com.santukis.datasources.mappers

import com.santukis.datasources.entities.dto.requests.DeckRequestDTO
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.entities.core.takeIfNotEmpty

fun DeckRequest.toDeckRequestDTO(): DeckRequestDTO =
    DeckRequestDTO(
        locale = regionality.locale.value,
        deckCode = deckCode.takeIfNotEmpty(),
        cardIds = cardIds.joinToString(","),
        heroId = heroId.takeIfNotEmpty()
    )