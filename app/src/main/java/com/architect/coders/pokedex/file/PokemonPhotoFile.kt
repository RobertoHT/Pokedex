package com.architect.coders.pokedex.file

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class PokemonPhotoFile(private val application: Application) {

    fun createFile(nameFile: String) : Pair<Uri, String> {
        val filePath: String
        val file = File.createTempFile(
            nameFile,
            ".jpg",
            getStorageDir()
        ).apply {
            filePath = absolutePath
        }

        val uri = FileProvider.getUriForFile(application, "com.architect.coders.pokedex.fileprovider", file)
        return Pair(uri, filePath)
    }

    fun deleteImageFile(filePath: String) {
        val fileTemp = File(filePath)
        fileTemp.delete()
    }

    private fun getStorageDir() =
        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
}