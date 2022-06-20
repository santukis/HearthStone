package com.santukis.datasources.mappers

import com.santukis.datasources.entities.dbo.*
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

fun Card.toCardDB(): CardDB =
    CardDB(
        identity = identity.toIdentityDB(),
        collectible = collectible?.name.orEmpty(),
        cardText = cardText.toCardTextDB(),
        cardStats = cardStats.toCardStatsDB(),
        image = images.toCardImageDB(),
        classId = cardClass.identity.id,
        cardTypeId = cardType.identity.id,
        cardSetId = cardSet.identity.id,
        rarityId = rarity.identity.id,
        multiClassIds = multiClassIds,
        childIds = childIds,
        parentId = parentId
    )

fun List<Keyword>.toCardToKeywordList(cardId: Int): List<CardDBToKeywordDB> =
    map { keyword ->
        CardDBToKeywordDB(
            cardId = cardId,
            keywordId = keyword.identity.id
        )
    }

fun CardSet.toCardSetDB(): CardSetDB =
    CardSetDB(
        identity = identity.toIdentityDB(),
        type = type.name,
        collectibleCount = collectibleCount.orDefault()
    )

fun GameMode.toGameModeDB(): GameModeDB =
    GameModeDB(
        identity = identity.toIdentityDB()
    )

fun Int.toArenaDB(): ArenaDB = ArenaDB(id = this)

fun CardType.toCardTypeDB(): CardTypeDB =
    CardTypeDB(
        identity = identity.toIdentityDB()
    )

fun List<GameMode>.toCardTypeToGameModeList(cardTypeId: Int): List<CardTypeDBToGameModeDB> =
    map { gameMode ->
        CardTypeDBToGameModeDB(
            cardTypeId = cardTypeId,
            gameModeId = gameMode.identity.id
        )
    }

fun Rarity.toRarityDB(): RarityDB =
    RarityDB(
        identity = identity.toIdentityDB(),
        craftingCost = craftingCost,
        dustValue = dustValue
    )

fun CardClass.toCardClassDB(): CardClassDB =
    CardClassDB(
        identity = identity.toIdentityDB(),
        cardId = cardId,
        heroPowerCardId = heroPowerCardId,
        alternativeHeroCardIds = alternativeHeroCardIds
    )

fun MinionType.toMinionTypeDB(): MinionTypeDB =
    MinionTypeDB(
        identity = identity.toIdentityDB()
    )

fun List<GameMode>.toMinionTypeToGameModeList(minionTypeId: Int): List<MinionTypeDBToGameModeDB> =
    map { gameMode ->
        MinionTypeDBToGameModeDB(
            minionTypeId = minionTypeId,
            gameModeId = gameMode.identity.id
        )
    }

fun SpellSchool.toSpellSchoolDB(): SpellSchoolDB =
    SpellSchoolDB(
        identity = identity.toIdentityDB()
    )

fun Keyword.toKeywordDB(): KeywordDB =
    KeywordDB(
        identity = identity.toIdentityDB(),
        cardText = cardText.toCardTextDB()
    )

fun List<GameMode>.toKeywordToGameModeList(keywordId: Int): List<KeywordDBToGameModeDB> =
    map { gameMode ->
        KeywordDBToGameModeDB(
            keywordId = keywordId,
            gameModeId = gameMode.identity.id
        )
    }

fun Deck.toDeckDB(): DeckDB =
    DeckDB(
        code = code,
        version = version,
        format = format,
        heroId = hero.identity.id,
        heroPowerId = heroPower.identity.id,
        classId = cardClass.identity.id,
        cardCount = cardCount
    )

fun List<Card>.toDeckToCardList(deckId: String): List<DeckDBToCardDB> =
    map { card ->
        DeckDBToCardDB(
            deckId = deckId,
            cardId = card.identity.id
        )
    }

private fun Identity.toIdentityDB(): IdentityDB =
    IdentityDB(
        id = id,
        slug = slug,
        name = name
    )

private fun CardText.toCardTextDB(): CardTextDB =
    CardTextDB(
        ruleText = ruleText,
        referenceText = referenceText,
        flavorText = flavorText
    )

private fun CardStats.toCardStatsDB(): CardStatsDB =
    CardStatsDB(
        manaCost = manaCost,
        health = health,
        attack = attack
    )

private fun CardImage.toCardImageDB(): CardImageDB =
    CardImageDB(
        image = image,
        imageGold = imageGold,
        cropImage = cropImage,
        artistName = artistName
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
