package com.santukis.datasources.local.daos

import androidx.room.Dao
import com.santukis.datasources.entities.dbo.MinionTypeDB
import com.santukis.datasources.entities.dbo.RarityDB

@Dao
interface MinionTypeDao: BaseDao<MinionTypeDB> {

}