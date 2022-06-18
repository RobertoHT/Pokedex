package com.architect.coders.pokedex.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type")
data class TypeL(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val pokemonID: Int,
    val name: String
)