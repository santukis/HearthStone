package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Rarity

data class RarityUI(
    val rarity: Rarity
) {

    fun rarity(): TextUI =
        TextUI(
            text = rarity.identity.name
        )
}