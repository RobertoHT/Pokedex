package com.architect.coders.pokedex.datasource

import com.architect.coders.pokedex.network.PokeClient

class PokemonRemoteDataSource {

    suspend fun getPokemonList(offset: Int) =
        PokeClient.service.getPokemonList(offset)

    suspend fun getPokemonDetail(pokemonID: Int) =
        PokeClient.service.getPokemonDetail(pokemonID)
}