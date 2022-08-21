package com.architect.coders.pokedex.framework.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonDetailL(
    @Embedded val pokemon: PokemonL,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonID"
    )
    val types: List<TypeL>,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonID"
    )
    val stats: List<StatL>
)