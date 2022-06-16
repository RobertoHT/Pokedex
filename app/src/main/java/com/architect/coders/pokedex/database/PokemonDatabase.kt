package com.architect.coders.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PokemonL::class, TypeL::class, StatL::class, CollectionL::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
}