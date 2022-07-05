package com.santukis.entities.hearthstone

data class CardImage(
    val image: String = "",
    val imageGold: String = "",
    val cropImage: String = "",
    val artistName: String = ""
) {

    fun asList(): List<String> {
        val images = mutableListOf<String>()

        image.takeIf { it.isNotEmpty() }?.let { images.add(it) }
        imageGold.takeIf { it.isNotEmpty() }?.let { images.add(it) }

        return images
    }
}