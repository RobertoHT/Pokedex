package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PokemonRepository

class SaveCollectionUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int, type: Int, image: String): Error? =
        repository.saveCollectionByPokemon(id, type, image)
}