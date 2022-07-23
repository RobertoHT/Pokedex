package com.architect.coders.pokedex.framework

import android.app.Application
import android.os.Environment
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.domain.Error
import java.io.File
import javax.inject.Inject

class FileRepository @Inject constructor(private val application: Application) : PhotoRepository {

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