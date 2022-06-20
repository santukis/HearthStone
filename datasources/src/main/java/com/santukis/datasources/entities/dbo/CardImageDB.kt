package com.santukis.datasources.entities.dbo

import com.santukis.entities.hearthstone.CardImage

data class CardImageDB(
    val image: String = "",
    val imageGold: String = "",
    val cropImage: String = "",
    val artistName: String = ""
) {

    fun toCardImage(): CardImage =
        CardImage(
            image = image,
            imageGold = imageGold,
            cropImage = cropImage,
            artistName = artistName
        )
}