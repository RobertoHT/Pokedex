package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PhotoRepository
import javax.inject.Inject

class GetPathUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    operator fun invoke(): String = photoRepository.path
}