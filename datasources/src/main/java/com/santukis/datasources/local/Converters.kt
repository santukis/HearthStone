package com.santukis.datasources.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromIdListToString(ids: List<Int>): String {
        return ids.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToIdList(ids: String): List<Int> {
        return ids.split(",").map { it.toIntOrNull() ?: 0 }
    }
}