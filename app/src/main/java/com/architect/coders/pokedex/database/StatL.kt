package com.architect.coders.pokedex.database

import androidx.room.Entity

@Entity(tableName = "stat")
data class StatL(
    val pokemonID: Int,
    val name: String,
    val baseStat: Int
)