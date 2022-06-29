package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.domain.Pokemon

interface PokemonRemoteDataSource {

    suspend fun getPokemonList(offset: Int): List<Pokemon>

    suspend fun getPokemonDetail(pokemonID: Int): Pokemon
}