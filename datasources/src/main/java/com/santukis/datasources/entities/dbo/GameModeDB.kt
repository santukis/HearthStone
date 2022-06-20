package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
   tableName = "gameModes",
   primaryKeys = ["id"]
)
data class GameModeDB(
   @Embedded
   val identity: IdentityDB = IdentityDB()
)
