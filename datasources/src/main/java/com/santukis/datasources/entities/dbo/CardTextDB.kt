package com.santukis.datasources.entities.dbo

import com.santukis.entities.hearthstone.CardText

data class CardTextDB(
    val ruleText: String = "",
    val referenceText: String = "",
    val flavorText: String = ""
) {

    fun toCardText(): CardText =
        CardText(
            ruleText = ruleText,
            referenceText = referenceText,
            flavorText = flavorText
        )
}