package com.santukis.datasources.entities.dto.requests


data class SearchCardsRequestDTO(
    /**
     * The locale to reflect in localized data.
     * If you do not supply a value, all translations are returned.
     */
    val locale: String,

    /**
     * The slug of the set the card belongs to.
     * If you do not supply a value, cards from all sets will be returned.
     */
    val set: String? = null,

    /**
     * The slug of the card's class.
     */
    val classSlug: String? = null,

    /**
     * The mana cost required to play the card.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val manaCost: Int? = null,

    /**
     * The attack power of the minion or weapon.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val attack: Int? = null,

    /**
     * The health of a minion.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val health: Int? = null,

    /**
     * Whether a card is collectible.
     * A value of 1 indicates that collectible cards should be returned; 0 indicates uncollectible cards.
     * To return all cards, use a value of '0,1'.
     */
    val collectible: String? = null,

    /**
     * The rarity of a card. This value must match the rarity slugs found in metadata.
     */
    val rarity: String? = null,

    /**
     * The type of card (for example, minion, spell, and so on).
     * This value must match the type slugs found in metadata.
     */
    val type: String? = null,

    /**
     * The type of minion card (for example, beast, murloc, dragon, and so on).
     * This value must match the minion type slugs found in metadata.
     */
    val minionType: String? = null,

    /**
     * A required keyword on the card (for example, battlecry, deathrattle, and so on).
     * This value must match the keyword slugs found in metadata.
     */
    val keyword: String? = null,

    /**
     * A text string used to filter cards. You must include a locale along with the textFilter parameter.
     */
    val textFilter: String? = null,

    /**
     * A recognized game mode (for example, battlegrounds or constructed).
     * The default value is constructed. See the [Game Modes Guide](https://develop.battle.net/documentation/hearthstone/guides/game-modes) for more information.
     */
    val gameMode: String? = null,

    /**
     * The school of a spell card (for example, arcane, fire, frost, and so on).
     * This value must match the spell school slugs found in metadata.
     */
    val spellSchool: String? = null,

    /**
     * item already loaded
     */
    val itemCount: Int = 0,

    /**
     * The sort option and direction used to sort the results.
     *
     * Valid values include
     *
     * manaCost:asc,
     * manaCost:desc,
     * attack:asc,
     * attack:desc,
     * health:asc,
     * health:desc,
     * class:asc,
     * class:desc,
     * groupByClass:asc,
     * groupByClass:desc,
     * name:asc,
     * name:desc.
     *
     * Results are sorted by name:asc by default.
     */
    val sort: String? = null
) {
}