package com.architect.coders.pokedex.usecases

import arrow.core.Either
import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class CreatePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    operator fun invoke(name: String): Either<Error, String> = photoRepository.createFile(name)
}