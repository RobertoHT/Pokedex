package com.architect.coders.pokedex.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): Flow<List<PokemonL>>

    @Transaction
    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun findPokemonByID(id: Int): PokemonDetailL

    @Query("SELECT COUNT(id) FROM pokemon")
    suspend fun pokemonCount(): Int

    @Insert
    suspend fun insertPokemon(pokemonList: List<PokemonL>)

    @Update
    fun updatePokemon(pokemonL: PokemonL)

    @Insert
    fun insertTypes(types: List<TypeL>)

    @Insert
    fun insertStats(stats: List<StatL>)

    @Query("SELECT * FROM collection WHERE pokemonID = :id")
    fun getAllCollectionByPokemon(id: Int): List<CollectionL>

    @Insert
    fun insertCollection(collection: CollectionL)
}