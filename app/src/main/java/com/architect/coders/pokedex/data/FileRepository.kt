package com.architect.coders.pokedex.data

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class FileRepository(private val application: Application) {

    val path: String = getStorageDir()?.absolutePath!!

    fun createFile(nameFile: String): Uri {
        val file = File.createTempFile(
            nameFile,
            ".jpg",
            getStorageDir()
        )

        return FileProvider.getUriForFile(
            application,
            "com.architect.coders.pokedex.fileprovider",
            file
        )
    }

    fun deleteImageFile(fileName: String) {
        val filePath = "$path/$fileName"
        val fileTemp = File(filePath)
        fileTemp.delete()
    }

    private fun getStorageDir() =
        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
}