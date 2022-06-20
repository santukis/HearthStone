package com.santukis.entities.hearthstone

data class Card(
    val identity: Identity = Identity(),
    val collectible: Collectible? = null,
    val cardClass: CardClass = CardClass(),
    val multiClassIds: List<Int> = emptyList(),
    val cardType: CardType = CardType(),
    val cardSet: CardSet = CardSet(),
    val rarity: Rarity = Rarity(),
    val cardStats: CardStats = CardStats(),
    val cardText: CardText = CardText(),
    val images: CardImage = CardImage(),
    val keywords: List<Keyword> = emptyList(),
    val childIds: List<Int> = emptyList(),
    val parentId: Int = -1
)