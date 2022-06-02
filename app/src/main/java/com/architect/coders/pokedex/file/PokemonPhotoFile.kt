package com.architect.coders.pokedex.file

import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.architect.coders.pokedex.model.DispatchPicture
import java.io.File

class PokemonPhotoFile(private val activity: AppCompatActivity) {

    private val dispatchPicture = DispatchPicture(activity)

    suspend fun takePhoto(nameFile: String) : String? {
        val pairFile = createFile(nameFile)
        val success = dispatchPicture.request(pairFile.first)
        return if (success) {
            pairFile.second
        } else {
            deleteImageFile(pairFile.second)
            null
        }
    }

    private fun createFile(nameFile: String) : Pair<Uri, String> {
        val filePath: String
        val file = File.createTempFile(
            nameFile,
            ".jpg",
            getStorageDir()
        ).apply {
            filePath = absolutePath
        }

        val uri = FileProvider.getUriForFile(activity, "com.architect.coders.pokedex.fileprovider", file)
        return Pair(uri, filePath)
    }

    private fun deleteImageFile(filePath: String) {
        val fileTemp = File(filePath)
        fileTemp.delete()
    }

    private fun getStorageDir() =
        activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
}