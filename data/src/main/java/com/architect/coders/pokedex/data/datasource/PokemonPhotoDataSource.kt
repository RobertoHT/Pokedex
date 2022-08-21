package com.architect.coders.pokedex.data.datasource

import arrow.core.Either
import com.architect.coders.pokedex.domain.Error

interface PokemonPhotoDataSource {

    val path: String

    fun createFile(nameFile: String): Either<Error, String>

    fun deleteImageFile(fileName: String): Error?
}