package com.architect.coders.pokedex.testshared

import com.architect.coders.pokedex.domain.*

val samplePokemon = Pokemon(
    0,
    "Name",
    5,
    10,
    false,
    listOf(Type("Type")),
    listOf(Stat("Stat", 10))
)

val sampleGallery = GalleryItem(PokeCollec.AMIIBO, mutableListOf("Photo"))