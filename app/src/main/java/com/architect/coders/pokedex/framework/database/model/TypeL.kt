package com.architect.coders.pokedex.framework.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type")
data class TypeL(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pokemonID: Int,
    val name: String
)