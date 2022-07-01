package com.santukis.datasources.entities.dbo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favourites",
    indices = [
        Index(value = ["cardId"], name = "favouriteIndex")
    ]
)
data class FavouriteDB(
    @PrimaryKey
    val cardId: Int
)