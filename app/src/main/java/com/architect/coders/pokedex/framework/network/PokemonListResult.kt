package com.architect.coders.pokedex.framework.network

import com.google.gson.annotations.SerializedName

data class PokemonListResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val pokemonItems: List<PokemonItemR>
)