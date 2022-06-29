package com.architect.coders.pokedex.usecases

import android.net.Uri
import arrow.core.Either
import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PhotoRepository

class CreatePhotoUseCase(private val photoRepository: PhotoRepository) {
    operator fun invoke(name: String): Either<Error, Uri> = photoRepository.createFile(name)
}