package com.architect.coders.pokedex.model

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DispatchPicture(activity: AppCompatActivity) {

    private var onRequest: (Boolean) -> Unit = {}
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        onRequest(success)
    }

    suspend fun request(uri: Uri): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                continuation.resume(it)
            }
            launcher.launch(uri)
        }
}