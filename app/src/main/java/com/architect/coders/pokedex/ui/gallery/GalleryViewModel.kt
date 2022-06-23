package com.architect.coders.pokedex.ui.gallery

import android.net.Uri
import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.common.toGalleryItem
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.CollectionL
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.util.getCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val pokemonID: Int,
    private val pokemonFile : PokemonPhotoFile,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            pokemonRepository.getCollectionByPokemon(pokemonID).collect { collectionList ->
                _state.value = UIState(colletionList = collectionList.toGalleryItem(pokemonFile.path))
            }
        }
    }

    fun onCreatePictureFile(@IdRes fabID: Int) {
        val pokeType = getCollection(fabID)
        val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
        val imageData = pokemonFile.createFile(nameFile)
        _state.value = _state.value.copy(nameImage = imageData.lastPathSegment, type = pokeType, uriImage = imageData)
    }

    fun onPictureReady(result: Boolean) {
        if (result) {
            viewModelScope.launch {
                pokemonRepository.saveCollectionByPokemon(
                    CollectionL(0, pokemonID, _state.value.type!!.id, _state.value.nameImage!!))
                onTakePictureDone()
            }
        } else {
            pokemonFile.deleteImageFile(_state.value.nameImage!!)
            onTakePictureDone()
        }
    }

    fun onUriDone() {
        _state.value = _state.value.copy(uriImage = null)
    }

    private fun onTakePictureDone() {
        _state.value = UIState(nameImage = null, type = null)
    }

    data class UIState(
        val colletionList: List<GalleryItem>? = null,
        val nameImage: String? = null,
        val type: PokeCollec? = null,
        val uriImage: Uri? = null
    )
}

@Suppress("UNCHECKED_CAST")
class GalleryViewModelFactory(
    private val pokemonID: Int,
    private val pokemonFile : PokemonPhotoFile,
    private val pokemoRepository: PokemonRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(pokemonID, pokemonFile, pokemoRepository) as T
    }
}