package com.architect.coders.pokedex.framework.network.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailR(
    @SerializedName("base_experience")
    val baseExperience: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("species")
    val species: Species,
    @SerializedName("stats")
    val stats: List<StatR>,
    @SerializedName("types")
    val types: List<TypeR>,
    @SerializedName("weight")
    val weight: Int
)