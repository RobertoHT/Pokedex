package com.architect.coders.pokedex.framework.file

import android.app.Application
import android.os.Environment
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.architect.coders.pokedex.data.datasource.PokemonPhotoDataSource
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.framework.toError
import java.io.File
import javax.inject.Inject

class PokemonFileDataSource @Inject constructor(private val application: Application) : PokemonPhotoDataSource {

    override val path: String = getStorageDir()?.absolutePath!!+"/"

    override fun createFile(nameFile: String): Either<Error, String> = try {
        val file = File.createTempFile(
            nameFile,
            ".jpg",
            getStorageDir()
        )

        file.absolutePath.right()
    } catch (e: Exception) {
        e.toError().left()
    }

    override fun deleteImageFile(fileName: String): Error? = try {
        val filePath = "$path$fileName"
        val fileTemp = File(filePath)
        fileTemp.delete()
        null
    } catch (e: Exception) {
        e.toError()
    }

    private fun getStorageDir() =
        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
}