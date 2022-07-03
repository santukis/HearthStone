package com.santukis.viewmodels.entities

import com.santukis.entities.hearthstone.CardClass
import com.santukis.viewmodels.R


fun CardClass?.getDrawable(): Int =
    when (this?.identity?.slug) {
        "demonhunter" -> R.drawable.demon_hunter_icon
        "druid" -> R.drawable.druid_icon
        "hunter" -> R.drawable.hunter_icon
        "mage" -> R.drawable.mage_icon
        "paladin" -> R.drawable.paladin_icon
        "priest" -> R.drawable.priest_icon
        "rogue" -> R.drawable.rogue_icon
        "shaman" -> R.drawable.shaman_icon
        "warlock" -> R.drawable.warlock_icon
        "warrior" -> R.drawable.warrior_icon
        "neutral" -> R.drawable.neutral_icon
        else -> R.drawable.empty
    }