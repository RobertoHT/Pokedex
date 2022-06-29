package com.architect.coders.pokedex.framework.network

import com.google.gson.annotations.SerializedName

data class PokemonItemR(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)