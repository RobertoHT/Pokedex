package com.architect.coders.pokedex.util

import androidx.annotation.IdRes
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.model.GalleryItem

fun getTypePokemonColor(typeName: String): Int {
    return when(typeName) {
        "bug" -> R.color.bug
        "dark" -> R.color.dark
        "dragon" -> R.color.dragon
        "electric" -> R.color.electric
        "fairy" -> R.color.fairy
        "fighting" -> R.color.fighting
        "fire" -> R.color.fire
        "flying" -> R.color.flying
        "ghost" -> R.color.ghost
        "grass" -> R.color.grass
        "ground" -> R.color.ground
        "ice" -> R.color.ice
        "poison" -> R.color.poison
        "psychic" -> R.color.psychic
        "rock" -> R.color.rock
        "steel" -> R.color.steel
        "water" -> R.color.water
        else -> R.color.white
    }
}

fun getStatPokemonText(statName: String): String {
    return when(statName) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SATK"
        "special-defense" -> "SDEF"
        "speed" -> "SPD"
        else -> ""
    }
}

fun getStatPokemonColor(statName: String): Int {
    return when(statName) {
        "hp" -> R.color.progress_hp
        "attack" -> R.color.progress_atk
        "defense" -> R.color.progress_def
        "special-attack" -> R.color.progress_satk
        "special-defense" -> R.color.progress_sdef
        "speed" -> R.color.progress_spd
        else -> R.color.white
    }
}

fun getGalleryItems(): List<GalleryItem> = listOf(
    GalleryItem(PokeCollec.AMIIBO, getList()),
    GalleryItem(PokeCollec.PLUSH, getList()),
    GalleryItem(PokeCollec.OTHER, getList()),
)

private fun getList(): MutableList<String> = mutableListOf(
    "https://placekitten.com/200/200?image=1",
    "https://placekitten.com/200/200?image=2",
    "https://placekitten.com/200/200?image=3",
    "https://placekitten.com/200/200?image=4",
    "https://placekitten.com/200/200?image=5",
    "https://placekitten.com/200/200?image=6",
    "https://placekitten.com/200/200?image=7",
    "https://placekitten.com/200/200?image=8",
    "https://placekitten.com/200/200?image=9",
    "https://placekitten.com/200/200?image=10"
)

fun getCollection(@IdRes fabID: Int) =
    when(fabID) {
        R.id.fabAmiibo -> PokeCollec.AMIIBO
        R.id.fabPlush -> PokeCollec.PLUSH
        R.id.fabOther -> PokeCollec.OTHER
        else -> PokeCollec.OTHER
    }