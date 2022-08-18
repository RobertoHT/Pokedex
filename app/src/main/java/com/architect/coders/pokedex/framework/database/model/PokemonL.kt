package com.architect.coders.pokedex.framework.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonL(
    @PrimaryKey val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val favorite: Boolean
)