package com.architect.coders.pokedex.ui.common

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class PictureUri(private val context: Context) {

    fun getUriFromFile(pathImage: String): Uri =
        FileProvider.getUriForFile(
            context,
            "com.architect.coders.pokedex.fileprovider",
            File(pathImage)
        )
}