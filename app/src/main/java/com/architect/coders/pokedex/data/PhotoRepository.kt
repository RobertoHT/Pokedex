package com.architect.coders.pokedex.data

import android.net.Uri
import arrow.core.Either
import com.architect.coders.pokedex.domain.Error

interface PhotoRepository {

    val path: String

    fun createFile(nameFile: String): Either<Error, Uri>

    fun deleteImageFile(fileName: String): Error?
}