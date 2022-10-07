package com.example.lab8.data.local_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab8.datasource.model.CharacterTable


@Database(entities = [CharacterTable::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}