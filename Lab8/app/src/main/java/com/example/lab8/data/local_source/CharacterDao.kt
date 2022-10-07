package com.example.lab8.data.local_source

import androidx.room.*
import com.example.lab8.datasource.model.CharacterTable

@Dao
interface CharacterDao {

    @Query("SELECT * FROM charactertable")
    suspend fun getAllCharacters(): List<CharacterTable>

    @Query("SELECT * FROM charactertable WHERE id = :id")
    suspend fun getCharacter(id: Int): CharacterTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterTable: CharacterTable)

    @Delete
    suspend fun deleteCharacter(characterTable: CharacterTable): Int

    @Update
    suspend fun updateCharacter(characterTable: CharacterTable)

    @Query("DELETE FROM charactertable")
    suspend fun deleteAll()
}