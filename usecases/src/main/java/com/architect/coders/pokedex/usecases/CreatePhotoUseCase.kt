package com.architect.coders.pokedex.usecases

import arrow.core.Either
import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class CreatePhotoUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(name: String): Either<Error, String> = repository.createPhoto(name)
}