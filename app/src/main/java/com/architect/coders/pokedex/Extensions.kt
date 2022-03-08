package com.architect.coders.pokedex

import com.architect.coders.pokedex.model.PokemonItem

fun PokemonItem.id() : Int {
    val split = url.split("/")
    return split[split.size - 2].toInt()
}

fun PokemonItem.imageUrl() : String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id()}.png"