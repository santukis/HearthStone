package com.santukis.datasources.entities.remote.dto


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
    val classes: List<ClassDTO>? = null,

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
)