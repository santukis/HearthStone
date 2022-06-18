package com.santukis.datasources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.santukis.datasources.entities.dbo.CardDB
import com.santukis.datasources.local.daos.CardDao

@Database(
    entities = [
        CardDB::class
    ],
    exportSchema = true, version = 1
)
@TypeConverters(Converters::class)
abstract class HearthstoneDatabase: RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var instance: HearthstoneDatabase? = null

        fun getInstance(context: Context): HearthstoneDatabase {
            synchronized(this) {
                return instance ?: kotlin.run {
                    Room.databaseBuilder(
                        context.applicationContext,
                        HearthstoneDatabase::class.java,
                        "HearthstoneDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                }.apply {
                    instance = this
                }
            }
        }
    }
}