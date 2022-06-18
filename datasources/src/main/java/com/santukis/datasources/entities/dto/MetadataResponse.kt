package com.santukis.datasources.entities.dto


import com.santukis.entities.hearthstone.Metadata
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetadataResponse(
    @Json(name = "sets")
    val sets: List<SetDTO>? = null,

    @Json(name = "setGroups")
    val setGroups: List<SetGroupDTO>? = null,

    @Json(name = "gameModes")
    val gameModes: List<GameModeDTO>? = null,

    @Json(name = "arenaIds")
    val arenaIds: List<Int>? = null,

    @Json(name = "types")
    val types: List<TypeDTO>? = null,

    @Json(name = "rarities")
    val rarities: List<RarityDTO>? = null,

    @Json(name = "classes")
    val classes: List<CardClassDTO>? = null,

    @Json(name = "minionTypes")
    val minionTypes: List<MinionTypeDTO>? = null,

    @Json(name = "spellSchools")
    val spellSchools: List<SpellSchoolDTO>? = null,

    @Json(name = "mercenaryRoles")
    val mercenaryRoles: List<MercenaryRoleDTO>? = null,

    @Json(name = "keywords")
    val keywords: List<KeywordDTO>? = null,

    @Json(name = "filterableFields")
    val filterableFields: List<String>? = null,

    @Json(name = "numericFields")
    val numericFields: List<String>? = null,

    @Json(name = "cardBackCategories")
    val cardBackCategories: List<CardBackCategoryDTO>? = null
) {

    fun toMetadata(): Metadata =
        Metadata(
            sets = sets?.map { it.toCardSet() }.orEmpty(),
            gameModes = gameModes?.map { it.toGameMode() }.orEmpty(),
            arenaIds = arenaIds.orEmpty(),
            types = types?.map { it.toCardType() }.orEmpty(),
            rarities = rarities?.map { it.toRarity() }.orEmpty(),
            classes = classes?.map { it.toCardClass() }.orEmpty(),
            minionTypes = minionTypes?.map { it.toMinionType() }.orEmpty(),
            spellSchools = spellSchools?.map { it.toSpellSchool() }.orEmpty(),
            keywords = keywords?.map { it.toKeyword() }.orEmpty()
        )
}