package com.architect.coders.pokedex.domain

data class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int = 0,
    val height: Int = 0,
    val favorite: Boolean = false,
    var types: List<Type> = emptyList(),
    var stats: List<Stat> = emptyList()
)
