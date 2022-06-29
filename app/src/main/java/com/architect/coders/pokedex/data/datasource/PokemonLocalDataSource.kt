package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.domain.GalleryItem
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.domain.Stat
import com.architect.coders.pokedex.domain.Type
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {

    val pokemonList: Flow<List<Pokemon>>

    suspend fun size(): Int

    suspend fun save(pokemonList: List<Pokemon>)

    suspend fun isEmpty(id: Int): Boolean

    fun findById(id: Int): Flow<Pokemon>

    suspend fun update(pokemon: Pokemon)

    suspend fun saveTypes(id: Int, types: List<Type>)

    suspend fun saveStats(id: Int, stats: List<Stat>)

    fun getCollectionById(id: Int, path: String): Flow<List<GalleryItem>>

    suspend fun saveCollection(id: Int, type: Int, image: String)
}