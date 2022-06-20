package com.santukis.datasources.entities.dto


import com.santukis.datasources.mappers.orDefault
import com.santukis.datasources.mappers.toSimplifiedIdentity
import com.santukis.entities.hearthstone.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardDTO(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "collectible")
    val collectible: String? = null,

    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "classId")
    val classId: Int? = null,

    @Json(name = "multiClassIds")
    val multiClassIds: List<Int>? = null,

    @Json(name = "cardTypeId")
    val cardTypeId: Int? = null,

    @Json(name = "cardSetId")
    val cardSetId: Int? = null,

    @Json(name = "rarityId")
    val rarityId: Int? = null,

    @Json(name = "artistName")
    val artistName: String? = null,

    @Json(name = "health")
    val health: Int? = null,

    @Json(name = "attack")
    val attack: Int? = null,

    @Json(name = "manaCost")
    val manaCost: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "image")
    val image: String? = null,

    @Json(name = "imageGold")
    val imageGold: String? = null,

    @Json(name = "flavorText")
    val flavorText: String? = null,

    @Json(name = "cropImage")
    val cropImage: String? = null,

    @Json(name = "keywordIds")
    val keywordIds: List<Int>? = null,

    @Json(name = "copyOfCardId")
    val copyOfCardId: Int? = null,

    @Json(name = "duels")
    val duels: DuelsDTO? = null,

    @Json(name = "childIds")
    val childIds: List<Int>? = null,

    @Json(name = "battlegrounds")
    val battlegrounds: BattlegroundsDTO? = null,

    @Json(name = "parentId")
    val parentId: Int? = null
) {

    fun toCard(): Card =
        Card(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            ),
            collectible = collectible.toCollectible(),
            cardClass = CardClass(identity = classId.toSimplifiedIdentity()),
            multiClassIds = multiClassIds.orEmpty(),
            cardType = CardType(identity = cardTypeId.toSimplifiedIdentity()),
            cardSet = CardSet(identity = cardSetId.toSimplifiedIdentity()),
            rarity = Rarity(identity = rarityId.toSimplifiedIdentity()),
            cardStats = CardStats(
                manaCost = manaCost.orDefault(),
                attack = attack.orDefault(),
                health = health.orDefault()
            ),
            cardText = CardText(
                ruleText = text.orEmpty(),
                flavorText = flavorText.orEmpty()
            ),
            images = CardImage(
                image = image.orEmpty(),
                imageGold = imageGold.orEmpty(),
                cropImage = cropImage.orEmpty(),
                artistName = artistName.orEmpty()
            ),
            keywords = keywordIds?.map { Keyword(identity = it.toSimplifiedIdentity()) }.orEmpty(),
            childIds = childIds.orEmpty(),
            parentId = parentId.orDefault()
        )

    private fun String?.toCollectible(): Collectible? =
        when(this) {
            "0" -> Collectible.Uncollectible
            "1" -> Collectible.Collectible
            "0,1" -> Collectible.All
            else -> null
        }
}