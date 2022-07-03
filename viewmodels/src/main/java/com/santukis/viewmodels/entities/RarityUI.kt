package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.Rarity
import com.santukis.viewmodels.R


fun Rarity?.getDrawable(): Int =
    when (this?.identity?.slug) {
        "common" -> R.drawable.common
        "rare" -> R.drawable.rare
        "epic" -> R.drawable.epic
        "legendary" -> R.drawable.legendary
        else -> R.drawable.empty
    }

fun Rarity?.getText(): TextUI =
    TextUI(
        text = this?.identity?.name.orEmpty(),
    )