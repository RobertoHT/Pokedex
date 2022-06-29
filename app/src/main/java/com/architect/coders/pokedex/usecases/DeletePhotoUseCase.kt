package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PhotoRepository

class DeletePhotoUseCase(private val photoRepository: PhotoRepository) {
    operator fun invoke(name: String): Error? = photoRepository.deleteImageFile(name)
}