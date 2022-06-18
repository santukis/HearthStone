package com.santukis.entities.hearthstone

data class CardStats(
    /**
     * The mana cost required to play the card.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val manaCost: Int = -1,

    /**
     * The attack power of the minion or weapon.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val attack: Int = -1,

    /**
     * The health of a minion.
     * You can include multiple values in a comma-separated list of numeric values.
     */
    val health: Int = -1
)