package com.architect.coders.pokedex.usecases

import android.net.Uri
import arrow.core.Either
import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.FileRepository

class CreatePhotoUseCase(private val fileRepository: FileRepository) {
    operator fun invoke(name: String): Either<Error, Uri> = fileRepository.createFile(name)
}