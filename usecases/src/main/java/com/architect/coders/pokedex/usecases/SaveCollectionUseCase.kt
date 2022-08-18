package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class SaveCollectionUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int, type: Int, image: String): Error? =
        repository.saveCollectionByPokemon(id, type, image)
}