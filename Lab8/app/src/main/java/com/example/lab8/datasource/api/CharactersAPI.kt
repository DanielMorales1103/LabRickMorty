package com.example.lab8.datasource.api

import com.example.lab8.datasource.model.AllApiResponse
import com.example.lab8.datasource.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CharactersAPI {

    @GET("/api/character")
    fun getCharacters() : Call<AllApiResponse>

    @GET("/api/character/{id}")
    fun getCharacter(
        @Path("id") id: Int
    ): Call<Character>
}