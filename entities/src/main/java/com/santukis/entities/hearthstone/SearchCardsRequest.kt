package com.santukis.entities.hearthstone


data class SearchCardsRequest(
    val regionality: Regionality,

    /**
     * The slug of the set the card belongs to.
     * If you do not supply a value, cards from all sets will be returned.
     */
    val set: CardSet? = null,

    /**
     * The card's class.
     */
    val cardClass: CardClass? = null,

    val cardStats: CardStats? = null,

    /**
     * Whether a card is collectible.
     */
    val collectible: Collectible = Collectible.Collectible,

    /**
     * The rarity of a card
     */
    val rarity: Rarity? = null,

    /**
     * The type of card.
     */
    val type: CardType? = null,

    /**
     * The type of minion card
     */
    val minionType: MinionType? = null,

    /**
     * A required keyword on the card (for example, battlecry, deathrattle, and so on).
     */
    val keyword: Keyword? = null,

    /**
     * A text string used to filter cards. You must include a locale along with the textFilter parameter.
     */
    val filter: String = "",

    /**
     * A recognized game mode (for example, battlegrounds or constructed).
     * The default value is constructed.
     * See the [Game Modes Guide](https://develop.battle.net/documentation/hearthstone/guides/game-modes) for more information.
     */
    val gameMode: GameMode? = null,

    /**
     * The school of a spell card (for example, arcane, fire, frost, and so on).
     */
    val spellSchool: SpellSchool? = null,

    /**
     * The sort filter and direction used to sort the results.
     *
     * Results are sorted by [SortFilter.Name] [Order.Ascendent] by default.
     */
    val sort: SortFilter? = null
)