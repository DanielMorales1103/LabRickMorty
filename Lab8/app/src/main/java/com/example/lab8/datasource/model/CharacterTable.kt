package com.example.lab8.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterTable (
    @PrimaryKey val id: Int,
    val episode: Int,
    val image: String,
    val name: String,
    val origin: String,
    val species: String,
    val gender: String,
    val status: String
    )

