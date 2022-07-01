package com.santukis.datasources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.santukis.datasources.entities.dbo.*
import com.santukis.datasources.local.daos.*

@Database(
    entities = [
        CardDB::class,
        CardClassDB::class,
        CardTypeDB::class,
        CardSetDB::class,
        GameModeDB::class,
        CardTypeDBToGameModeDB::class,
        RarityDB::class,
        KeywordDB::class,
        KeywordDBToGameModeDB::class,
        CardDBToKeywordDB::class,
        MinionTypeDB::class,
        MinionTypeDBToGameModeDB::class,
        SpellSchoolDB::class,
        ArenaDB::class,
        DeckDB::class,
        DeckDBToCardDB::class,
        FavouriteDB::class
    ],
    exportSchema = true, version = 3
)
@TypeConverters(Converters::class)
abstract class HearthstoneDatabase: RoomDatabase() {

    abstract fun cardDao(): CardDao

    abstract fun cardClassDao(): CardClassDao

    abstract fun cardSetDao(): CardSetDao

    abstract fun cardTypeDao(): CardTypeDao

    abstract fun gameModeDao(): GameModeDao

    abstract fun cardTypeToGameModeDao(): CardTypeToGameModeDao

    abstract fun rarityDao(): RarityDao

    abstract fun keywordDao(): KeywordDao

    abstract fun keywordToGameModeDao(): KeywordToGameModeDao

    abstract fun cardToKeywordDao(): CardToKeywordDao

    abstract fun minionTypeDao(): MinionTypeDao

    abstract fun minionTypeToGameModeDao(): MinionTypeToGameModeDao

    abstract fun spellSchoolDao(): SpellSchoolDao

    abstract fun arenaDao(): ArenaDao

    abstract fun deckDao(): DeckDao

    abstract fun deckToCardDao(): DeckToCardDao

    abstract fun favouriteDao(): FavouriteDao

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