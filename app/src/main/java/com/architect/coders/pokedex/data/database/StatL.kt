package com.architect.coders.pokedex.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stat")
data class StatL(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pokemonID: Int,
    val name: String,
    val baseStat: Int
)