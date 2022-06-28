package com.architect.coders.pokedex.data

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.File

class FileRepository(private val application: Application) {

    val path: String = getStorageDir()?.absolutePath!!

    fun createFile(nameFile: String): Either<Error, Uri> = try {
        val file = File.createTempFile(
            nameFile,
            ".jpg",
            getStorageDir()
        )

        FileProvider.getUriForFile(
            application,
            "com.architect.coders.pokedex.fileprovider",
            file
        ).right()
    } catch (e: Exception) {
        e.toError().left()
    }

    fun deleteImageFile(fileName: String): Error? = tryCall {
        val filePath = "$path/$fileName"
        val fileTemp = File(filePath)
        fileTemp.delete()
    }

    private fun getStorageDir() =
        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
}