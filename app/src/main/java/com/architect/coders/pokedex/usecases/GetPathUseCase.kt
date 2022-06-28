package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.FileRepository

class GetPathUseCase(private val fileRepository: FileRepository) {
    operator fun invoke(): String = fileRepository.path
}