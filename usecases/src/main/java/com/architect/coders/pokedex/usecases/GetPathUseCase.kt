package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import javax.inject.Inject

class GetPathUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(): String = repository.getPhotoPath()
}