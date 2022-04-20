package com.architect.coders.pokedex.data

import com.architect.coders.pokedex.network.PokeClient

class PokemonRepository() {

    suspend fun getPokemonList(offset: Int) =
        PokeClient.service.getPokemonList(offset)

    suspend fun getPokemonDetail(pokemonID: Int) =
        PokeClient.service.getPokemonDetail(pokemonID)
}