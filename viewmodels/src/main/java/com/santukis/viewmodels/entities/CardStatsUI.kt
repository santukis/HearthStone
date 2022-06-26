package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.santukis.entities.hearthstone.CardStats

data class CardStatsUI(
    private val cardStats: CardStats
) {

    fun mana(): TextUI =
        TextUI(
            text = cardStats.manaCost.toString(),
            style = TextStyle(
                color = Color.Blue
            )
        )

    fun attack(): TextUI =
        TextUI(
            text = cardStats.attack.toString(),
            style = TextStyle(
                color = Color.Yellow
            )
        )

    fun health(): TextUI =
        TextUI(
            text = cardStats.health.toString(),
            style = TextStyle(
                color = Color.Red
            )
        )
}