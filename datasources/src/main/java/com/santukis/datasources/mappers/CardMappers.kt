package com.santukis.datasources.mappers

import com.santukis.datasources.entities.dto.CardDTO
import com.santukis.datasources.entities.dto.CardsResponse
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

fun CardsResponse.toCardList(): List<Card> = cards?.map { it.toCard() }.orEmpty()

private fun CardDTO.toCard(): Card =
    Card(
        identity = Identity(
            id = id.orDefault(),
            slug = slug.orEmpty(),
            name = name.orEmpty()
        ),
        collectible = collectible.toCollectible(),
        cardClass = CardClass(identity = classId.toSimplifiedIdentity()),
        multiClassIds = multiClassIds.orEmpty(),
        cardType = CardType(identity = cardTypeId.toSimplifiedIdentity()),
        cardSet = CardSet(identity = cardSetId.toSimplifiedIdentity()),
        rarity = Rarity(identity = rarityId.toSimplifiedIdentity()),
        cardStats = CardStats(
            manaCost = manaCost.orDefault(),
            attack = attack.orDefault(),
            health = health.orDefault()
        ),
        cardText = CardText(
            ruleText = text.orEmpty(),
            flavorText = flavorText.orEmpty()
        ),
        images = CardImage(
            image = image.orEmpty(),
            imageGold = imageGold.orEmpty(),
            cropImage = cropImage.orEmpty(),
            artistName = artistName.orEmpty()
        ),
        keywords = keywordIds?.map { Keyword(identity = it.toSimplifiedIdentity()) }.orEmpty(),
        childIds = childIds.orEmpty()
    )

private fun String?.toCollectible(): Collectible? =
    when(this) {
        "0" -> Collectible.Uncollectible
        "1" -> Collectible.Collectible
        "0,1" -> Collectible.All
        else -> null
    }

private fun Int?.toSimplifiedIdentity(): Identity = Identity(id = this.orDefault())
