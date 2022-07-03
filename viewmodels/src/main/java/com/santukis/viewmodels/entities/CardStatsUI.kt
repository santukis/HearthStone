package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.santukis.entities.hearthstone.CardStats

fun CardStats.mana(): TextUI =
    TextUI(
        text = manaCost.toString(),
        style = TextStyle(
            color = Color.Blue
        )
    )

fun CardStats.attack(): TextUI =
    TextUI(
        text = attack.toString(),
        style = TextStyle(
            color = Color.Yellow
        )
    )

fun CardStats.health(): TextUI =
    TextUI(
        text = health.toString(),
        style = TextStyle(
            color = Color.Red
        )
    )