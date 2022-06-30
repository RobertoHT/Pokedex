package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.Error

class SaveCollectionUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int, type: Int, image: String): Error? =
        repository.saveCollectionByPokemon(id, type, image)
}