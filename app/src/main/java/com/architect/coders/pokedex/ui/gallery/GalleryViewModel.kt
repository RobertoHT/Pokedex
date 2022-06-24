package com.architect.coders.pokedex.ui.gallery

import android.net.Uri
import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.common.toGalleryItem
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.CollectionL
import com.architect.coders.pokedex.data.FileRepository
import com.architect.coders.pokedex.model.Error
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.model.toError
import com.architect.coders.pokedex.util.getCollection
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val pokemonID: Int,
    private val fileRepository : FileRepository,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            pokemonRepository.getCollectionByPokemon(pokemonID)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { collectionList ->
                    _state.update { it.copy(colletionList = collectionList.toGalleryItem(fileRepository.path)) }
            }
        }
    }

    fun onCreatePictureFile(@IdRes fabID: Int) {
        val pokeType = getCollection(fabID)
        val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
        val imageData = fileRepository.createFile(nameFile)
        imageData.fold({ cause -> _state.update { it.copy(error = cause) } }) { uri ->
            _state.update { it.copy(nameImage = uri.lastPathSegment, type = pokeType, uriImage = uri) }
        }
    }

    fun onPictureReady(result: Boolean) {
        if (result) {
            viewModelScope.launch {
                val cause = pokemonRepository.saveCollectionByPokemon(
                    CollectionL(0, pokemonID, _state.value.type!!.id, _state.value.nameImage!!))
                _state.update { it.copy(error = cause) }
                onTakePictureDone()
            }
        } else {
            val cause = fileRepository.deleteImageFile(_state.value.nameImage!!)
            _state.update { it.copy(error = cause) }
            onTakePictureDone()
        }
    }

    fun onUriDone() {
        _state.update { it.copy(uriImage = null) }
    }

    private fun onTakePictureDone() {
        _state.update { it.copy(nameImage = null, type = null) }
    }

    data class UIState(
        val colletionList: List<GalleryItem>? = null,
        val nameImage: String? = null,
        val type: PokeCollec? = null,
        val uriImage: Uri? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class GalleryViewModelFactory(
    private val pokemonID: Int,
    private val fileRepository : FileRepository,
    private val pokemoRepository: PokemonRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(pokemonID, fileRepository, pokemoRepository) as T
    }
}