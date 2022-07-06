package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    operator fun invoke(name: String): Error? = photoRepository.deleteImageFile(name)
}