package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.domain.*

internal val samplePokemon = Pokemon(
    0,
    "Name",
    5,
    10,
    false,
    listOf(Type("Type")),
    listOf(Stat("Stat", 10))
)

internal val sampleGallery = GalleryItem(PokeCollec.AMIIBO, mutableListOf("Photo"))