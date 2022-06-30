package com.architect.coders.pokedex.framework.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): Flow<List<PokemonL>>

    @Query("SELECT COUNT(id) FROM pokemon")
    suspend fun pokemonCount(): Int

    @Insert
    suspend fun insertPokemon(pokemonList: List<PokemonL>)

    @Query("SELECT COUNT(id) FROM pokemon WHERE id = :id AND weight = 0 AND height = 0")
    suspend fun isPokemonByIDEmpty(id: Int): Int

    @Update
    suspend fun updatePokemon(pokemonL: PokemonL)

    @Insert
    suspend fun insertTypes(types: List<TypeL>)

    @Insert
    suspend fun insertStats(stats: List<StatL>)

    @Transaction
    @Query("SELECT * FROM pokemon WHERE id = :id AND weight != 0 AND height != 0")
    fun findPokemonByID(id: Int): Flow<PokemonDetailL>

    @Query("SELECT * FROM collection WHERE pokemonID = :id ORDER BY type")
    fun getAllCollectionByPokemon(id: Int): Flow<List<CollectionL>>

    @Insert
    suspend fun insertCollection(collection: CollectionL)
}