package com.architect.coders.pokedex.database

import androidx.room.Entity

@Entity(tableName = "collection")
data class CollectionL(
    val pokemonID: Int,
    val type: Int,
    val photo: String
)