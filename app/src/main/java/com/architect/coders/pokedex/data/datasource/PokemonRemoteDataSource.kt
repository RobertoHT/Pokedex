package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.data.network.PokeClient

class PokemonRemoteDataSource {

    suspend fun getPokemonList(offset: Int) =
        PokeClient.service.getPokemonList(offset)

    suspend fun getPokemonDetail(pokemonID: Int) =
        PokeClient.service.getPokemonDetail(pokemonID)
}