package com.architect.coders.pokedex.database

import androidx.room.Entity

@Entity(tableName = "type")
data class TypeL(
    val pokemonID: Int,
    val name: String
)