package com.architect.coders.pokedex.framework.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("pokemon?limit=30")
    suspend fun getPokemonList(@Query("offset") offset: Int): PokemonListResult

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") id: Int): PokemonDetailR
}