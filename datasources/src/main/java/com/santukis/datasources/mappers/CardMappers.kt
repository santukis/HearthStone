package com.santukis.datasources.mappers

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.santukis.datasources.entities.dbo.*
import com.santukis.datasources.entities.dto.requests.SearchCardsRequestDTO
import com.santukis.entities.hearthstone.*
import com.santukis.entities.core.orDefault
import com.santukis.entities.core.takeIfNotDefault
import com.santukis.entities.core.takeIfNotEmpty
import com.santukis.entities.paging.PagingData

fun SearchCardsRequest.toSearchCardsRequestDTO(): SearchCardsRequestDTO =
    SearchCardsRequestDTO(
        locale = regionality.locale.value,
        set = set?.identity?.slug?.takeIfNotEmpty(),
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
        sort = sort?.toQuery()
    )

fun SearchCardsRequest.toSqliteQuery(pagingData: PagingData): SupportSQLiteQuery {
    var query = "SELECT * FROM cards"
    query = buildStatements(query)

    query += " ORDER BY id LIMIT ${pagingData.pageSize} OFFSET ${pagingData.itemCount}"

    println("Query -> $query")

    return SimpleSQLiteQuery(query)
}

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
        spellSchoolId = spellSchool.identity.id,
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

private fun SearchCardsRequest.buildStatements(initialQuery: String): String {
    var query = initialQuery
    val statements = mutableListOf<String>()

    set?.let { statements.add("cardSetId = ${it.identity.id}") }

    cardClass?.let { statements.add("classId = ${it.identity.id}") }

    cardStats?.let { stats ->
        stats.manaCost.takeIf { cost -> cost >= 0 }?.let { statements.add("manaCost = $it") }
        stats.attack.takeIf { attack -> attack >= 0 }?.let { statements.add("attack = $it") }
        stats.health.takeIf { health -> health >= 0 }?.let { statements.add("health = $it") }
    }

    collectible.takeIf { it != Collectible.All }?.let { statements.add("collectible = ${it.name}") }

    rarity?.let { statements.add("rarityId = ${it.identity.id}") }

    type?.let { statements.add("cardTypeId = ${it.identity.id}") }

    keyword?.let {
        statements.add("id IN (SELECT cardId FROM cardsToKeywords WHERE keywordId = ${it.identity.id})")
    }

    spellSchool?.let { statements.add("spellSchoolId = ${it.identity.id}") }

    filter.takeIfNotEmpty()?.let { statements.add("ruleText LIKE '%' || $it || '%'") }

    statements.takeIf { it.isNotEmpty() }?.let {
        query += " WHERE ".plus(it.joinToString(separator = " AND "))
    }

    return query
}
