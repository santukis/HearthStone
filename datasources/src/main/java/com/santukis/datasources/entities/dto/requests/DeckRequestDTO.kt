package com.santukis.datasources.entities.dto.requests

data class DeckRequestDTO(
    /**
     * The locale to reflect in localized data.
     * e.g = "en_US"
     */
    val locale: String,

    /**
     * A code that identifies a deck.
     * You can copy one from the game or various Hearthstone websites.
     * The value should be URL encoded.
     * e.g = "AAECAQcG+wyd8AKS+AKggAOblAPanQMMS6IE/web8wLR9QKD+wKe+wKz/AL1gAOXlAOalAOSnwMA"
     */
    val deckCode: String? = null,

    /**
     * A list of card IDs representing cards in the deck.
     * Ignored if a code parameter is also present.
     * e.g = "906,1099,1362"
     */
    val cardIds: String? = null,

    /**
     * The card ID for the hero of the deck. Used along with ids.
     * If not present, the API will attempt to add a default hero and class based on the cards in the deck.
     */
    val heroId: String? = null
)
