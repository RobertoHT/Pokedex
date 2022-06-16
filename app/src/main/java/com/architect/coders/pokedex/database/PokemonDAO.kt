package com.architect.coders.pokedex.database

import androidx.room.*

@Dao
interface PokemonDAO {

    @Query("SELECT id FROM pokemon")
    fun getAllPokemon(): List<Int>

    @Transaction
    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun findPokemonByID(id: Int): PokemonDetailL

    @Query("SELECT COUNT(id) FROM pokemon")
    fun pokemonCount(): Int

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