package com.architect.coders.pokedex.network


import com.google.gson.annotations.SerializedName

data class StatR(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: StatX
)