package com.architect.coders.pokedex.ui.gallery

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.architect.coders.pokedex.model.DispatchPicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildGalleryState(
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    dispatchPicture: DispatchPicture = DispatchPicture(this)
) = GalleryState(scope, navController, dispatchPicture)

class GalleryState(
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val dispatchPicture: DispatchPicture
) {

    fun onImageClicked(imageSrc: String) {
        val action = GalleryFragmentDirections.actionGalleryToCollection(imageSrc)
        navController.navigate(action)
    }

    fun onTakePicture(uriImage: Uri, afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = dispatchPicture.request(uriImage)
            afterRequest(result)
        }
    }
}