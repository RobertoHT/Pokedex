package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.FileRepository

class DeletePhotoUseCase(private val fileRepository: FileRepository) {
    operator fun invoke(name: String): Error? = fileRepository.deleteImageFile(name)
}