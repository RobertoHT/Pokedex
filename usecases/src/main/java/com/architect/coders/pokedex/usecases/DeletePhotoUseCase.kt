package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(name: String): Error? = repository.deletePhoto(name)
}