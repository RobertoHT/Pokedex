package com.architect.coders.pokedex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PokeService = retrofit.create(PokeService::class.java)
}