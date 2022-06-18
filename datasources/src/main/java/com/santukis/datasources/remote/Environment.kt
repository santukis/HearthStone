package com.santukis.datasources.remote

import com.santukis.entities.hearthstone.Region

sealed class Environment(
    val authApi: String,
    val blizzardApi: String
) {

    class Pro: Environment(authApi = "https://us.battle.net/", blizzardApi = "https://{region}.api.blizzard.com/")

    class Testing(baseUrl: String): Environment(baseUrl, baseUrl)


    fun deckEndpoint(region: Region): String = "${getBlizzardBaseUrl(region)}hearthstone/deck"

    fun card(region: Region, cardId: String): String = "${getBlizzardBaseUrl(region)}hearthstone/cards/$cardId"

    fun searchCards(region: Region): String = "${getBlizzardBaseUrl(region)}hearthstone/cards"

    fun cardBacks(region: Region): String = "${getBlizzardBaseUrl(region)}hearthstone/cardbacks"

    fun metadata(region: Region): String = "${getBlizzardBaseUrl(region)}hearthstone/metadata"

    private fun getBlizzardBaseUrl(region: Region) = blizzardApi.replace("{region}", region.value)
}