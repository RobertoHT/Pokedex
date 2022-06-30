package com.architect.coders.pokedex.ui.gallery

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.architect.coders.pokedex.ui.common.PictureRequester
import com.architect.coders.pokedex.ui.common.PictureUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildGalleryState(
    context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    uriPicture: PictureUri = PictureUri(context),
    dispatchPicture: PictureRequester = PictureRequester(this)
) = GalleryState(scope, navController, uriPicture, dispatchPicture)

class GalleryState(
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val uriPicture: PictureUri,
    private val dispatchPicture: PictureRequester
) {

    fun onImageClicked(imageSrc: String) {
        val action = GalleryFragmentDirections.actionGalleryToCollection(imageSrc)
        navController.navigate(action)
    }

    fun onTakePicture(pathImage: String, afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = dispatchPicture.request(uriPicture.getUriFromFile(pathImage))
            afterRequest(result)
        }
    }
}