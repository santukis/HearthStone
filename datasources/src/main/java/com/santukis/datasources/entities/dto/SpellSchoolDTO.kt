package com.santukis.datasources.entities.dto


import com.santukis.entities.core.orDefault
import com.santukis.entities.hearthstone.Identity
import com.santukis.entities.hearthstone.SpellSchool
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpellSchoolDTO(
    @Json(name = "slug")
    val slug: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null
) {
    fun toSpellSchool(): SpellSchool =
        SpellSchool(
            identity = Identity(
                id = id.orDefault(),
                slug = slug.orEmpty(),
                name = name.orEmpty()
            )
        )
}