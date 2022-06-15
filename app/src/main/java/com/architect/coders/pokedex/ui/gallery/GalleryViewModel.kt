package com.architect.coders.pokedex.ui.gallery

import android.net.Uri
import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.util.getCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GalleryViewModel(
    private val pokemonID: Int,
    private val pokemonFile : PokemonPhotoFile
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    fun onCreatePictureFile(@IdRes fabID: Int) {
        val pokeType = getCollection(fabID)
        val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
        val imageData = pokemonFile.createFile(nameFile)
        _state.value = _state.value.copy(pokeType = pokeType, uriImage = imageData.first, pathImage = imageData.second)
    }

    fun onPictureReady(result: Boolean) {
        if (result) {
            _state.value = _state.value.copy(photo = Pair(_state.value.pokeType!!, _state.value.pathImage!!))
        } else {
            pokemonFile.deleteImageFile(_state.value.pathImage!!)
            onTakePictureDone()
        }
    }

    fun onUriDone() {
        _state.value = _state.value.copy(uriImage = null)
    }

    fun onTakePictureDone() {
        _state.value = UIState()
    }

    data class UIState(
        val photo: Pair<PokeCollec, String>? = null,
        val pokeType: PokeCollec? = null,
        val uriImage: Uri? = null,
        val pathImage: String? = null
    )
}

@Suppress("UNCHECKED_CAST")
class GalleryViewModelFactory(
    private val pokemonID: Int,
    private val pokemonFile : PokemonPhotoFile
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(pokemonID, pokemonFile) as T
    }
}