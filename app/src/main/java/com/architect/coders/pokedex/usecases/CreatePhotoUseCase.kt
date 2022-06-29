package com.architect.coders.pokedex.usecases

import android.net.Uri
import arrow.core.Either
import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.domain.Error

class CreatePhotoUseCase(private val photoRepository: PhotoRepository) {
    operator fun invoke(name: String): Either<Error, Uri> = photoRepository.createFile(name)
}