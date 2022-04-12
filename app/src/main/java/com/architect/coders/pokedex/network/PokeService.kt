package com.architect.coders.pokedex.network

import com.architect.coders.pokedex.model.PokemonDetail
import com.architect.coders.pokedex.model.PokemonListResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("pokemon?limit=20")
    suspend fun getPokemonList(@Query("offset") offset: Int): PokemonListResult

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") id: Int): PokemonDetail
}