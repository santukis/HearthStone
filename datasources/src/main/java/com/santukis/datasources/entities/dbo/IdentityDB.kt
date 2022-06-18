package com.santukis.datasources.entities.dbo

import com.santukis.entities.hearthstone.Identity

data class IdentityDB(
    val id: Int = -1,
    val slug: String = "",
    val name: String = ""
) {

    fun toIdentity(): Identity =
        Identity(
            id = id,
            slug = slug,
            name = name
        )
}
