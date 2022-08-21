package com.architect.coders.pokedex.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.architect.coders.pokedex.framework.database.model.CollectionL
import com.architect.coders.pokedex.framework.database.model.PokemonL
import com.architect.coders.pokedex.framework.database.model.StatL
import com.architect.coders.pokedex.framework.database.model.TypeL

@Database(
    entities = [PokemonL::class, TypeL::class, StatL::class, CollectionL::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
}