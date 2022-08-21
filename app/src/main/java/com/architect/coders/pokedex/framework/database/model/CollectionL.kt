package com.architect.coders.pokedex.framework.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class CollectionL(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val pokemonID: Int,
    val type: Int,
    val photo: String
)