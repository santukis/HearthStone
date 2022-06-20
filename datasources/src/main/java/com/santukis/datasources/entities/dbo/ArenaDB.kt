package com.santukis.datasources.entities.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "arenaIds"
)
data class ArenaDB(
    @PrimaryKey
    val id: Int
)