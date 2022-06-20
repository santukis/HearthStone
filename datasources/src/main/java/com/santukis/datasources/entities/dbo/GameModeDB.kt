package com.santukis.datasources.entities.dbo

import androidx.room.Embedded
import androidx.room.Entity
import com.santukis.entities.hearthstone.GameMode

@Entity(
   tableName = "gameModes",
   primaryKeys = ["id"]
)
data class GameModeDB(
   @Embedded
   val identity: IdentityDB = IdentityDB()
) {

   fun toGameMode(): GameMode =
      GameMode(
         identity = identity.toIdentity()
      )
}
