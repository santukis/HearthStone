package com.santukis.datasources.entities.dbo

import com.santukis.entities.hearthstone.CardStats

data class CardStatsDB(
    val manaCost: Int = -1,
    val attack: Int = -1,
    val health: Int = -1
) {

    fun toCardStats(): CardStats =
        CardStats(
            manaCost = manaCost,
            attack = attack,
            health = health
        )
}