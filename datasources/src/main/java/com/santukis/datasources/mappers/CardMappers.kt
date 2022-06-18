package com.santukis.datasources.mappers

import com.santukis.datasources.entities.dto.requests.SearchCardsRequestDTO
import com.santukis.entities.hearthstone.*

fun SearchCardsRequest.toSearchCardsRequestDTO(): SearchCardsRequestDTO =
    SearchCardsRequestDTO(
        locale = regionality.locale.value,
        set = set.takeIfNotEmpty(),
        classSlug = cardClass?.identity?.slug?.takeIfNotEmpty(),
        manaCost = cardStats?.manaCost?.takeIfNotDefault(),
        attack = cardStats?.attack?.takeIfNotDefault(),
        health = cardStats?.health?.takeIfNotDefault(),
        collectible = collectible.toQuery(),
        rarity = rarity?.identity?.slug?.takeIfNotEmpty(),
        type = type?.identity?.slug?.takeIfNotEmpty(),
        minionType = minionType?.identity?.slug?.takeIfNotEmpty(),
        keyword = keyword?.identity?.slug?.takeIfNotEmpty(),
        textFilter = filter.takeIfNotEmpty(),
        gameMode = gameMode?.identity?.slug?.takeIfNotEmpty(),
        spellSchool = spellSchool?.identity?.slug?.takeIfNotEmpty(),
        page = page.takeIfNotDefault(),
        pageSize = pageSize.takeIfNotDefault(),
        sort = sort?.toQuery()
    )

fun Int?.toSimplifiedIdentity(): Identity = Identity(id = this.orDefault())

fun List<Int>?.toGameModeList(): List<GameMode> = this?.map { GameMode(identity = it.toSimplifiedIdentity()) }.orEmpty()

private fun Collectible.toQuery(): String? =
    when {
        this == Collectible.Collectible -> "1"
        this == Collectible.Uncollectible -> "0"
        this == Collectible.All -> "0,1"
        else -> null
    }

private fun SortFilter.toQuery(): String {
    val filter = when (this) {
        is SortFilter.ManaCost -> "manaCost"
        is SortFilter.Attack -> "attack"
        is SortFilter.Health -> "health"
        is SortFilter.HeroClass -> "class"
        is SortFilter.GroupByClass -> "groupByClass"
        is SortFilter.Name -> "name"
    }

    return "$filter:${order.toQuery()}"
}

private fun Order.toQuery(): String =
    when (Order.Ascendent) {
        this -> "asc"
        else -> "desc"
    }
