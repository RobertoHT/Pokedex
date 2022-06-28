package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.data.database.*
import kotlinx.coroutines.flow.Flow

class PokemonLocalDataSource(private val pokemonDao: PokemonDAO) {

    val pokemonList: Flow<List<PokemonL>> = pokemonDao.getAllPokemon()

    suspend fun size(): Int = pokemonDao.pokemonCount()

    suspend fun save(pokemonList: List<PokemonL>) {
        pokemonDao.insertPokemon(pokemonList)
    }

    suspend fun isEmpty(id: Int): Boolean = pokemonDao.isPokemonByIDEmpty(id) == 1

    fun findById(id: Int): Flow<PokemonDetailL> = pokemonDao.findPokemonByID(id)

    suspend fun update(pokemon: PokemonL) = pokemonDao.updatePokemon(pokemon)

    suspend fun saveTypes(types: List<TypeL>) = pokemonDao.insertTypes(types)

    suspend fun saveStats(stats: List<StatL>) = pokemonDao.insertStats(stats)

    fun getCollectionById(id: Int): Flow<List<CollectionL>> = pokemonDao.getAllCollectionByPokemon(id)

    suspend fun saveCollection(collection: CollectionL) = pokemonDao.insertCollection(collection)
}