package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PhotoRepository

class GetPathUseCase(private val photoRepository: PhotoRepository) {
    operator fun invoke(): String = photoRepository.path
}