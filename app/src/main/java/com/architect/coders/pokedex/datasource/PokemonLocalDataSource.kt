package com.architect.coders.pokedex.datasource

import com.architect.coders.pokedex.database.PokemonDAO
import com.architect.coders.pokedex.database.PokemonL
import kotlinx.coroutines.flow.Flow

class PokemonLocalDataSource(private val pokemonDao: PokemonDAO) {

    val pokemonList: Flow<List<PokemonL>> = pokemonDao.getAllPokemon()

    suspend fun size(): Int = pokemonDao.pokemonCount()

    suspend fun save(pokemonList: List<PokemonL>) {
        pokemonDao.insertPokemon(pokemonList)
    }
}