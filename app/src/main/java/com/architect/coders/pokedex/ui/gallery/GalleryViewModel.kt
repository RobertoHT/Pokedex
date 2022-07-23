package com.architect.coders.pokedex.ui.gallery

import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.di.PokemonId
import com.architect.coders.pokedex.domain.PokeCollec
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.GalleryItem
import com.architect.coders.pokedex.usecases.CreatePhotoUseCase
import com.architect.coders.pokedex.usecases.DeletePhotoUseCase
import com.architect.coders.pokedex.usecases.GetPathUseCase
import com.architect.coders.pokedex.framework.toError
import com.architect.coders.pokedex.ui.common.getCollection
import com.architect.coders.pokedex.ui.common.lastPathSegment
import com.architect.coders.pokedex.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    @PokemonId private val pokemonID: Int,
    private val findCollectionsUseCase: FindCollectionsUseCase,
    private val saveCollectionUseCase: SaveCollectionUseCase,
    private val getPathUseCase: GetPathUseCase,
    private val createPhotoUseCase: CreatePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            findCollectionsUseCase(pokemonID, getPathUseCase())
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { collectionList ->
                    _state.update { it.copy(colletionList = collectionList) }
            }
        }
    }

    fun onCreatePictureFile(@IdRes fabID: Int) {
        viewModelScope.launch {
            val pokeType = getCollection(fabID)
            val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
            val imageData = createPhotoUseCase(nameFile)
            imageData.fold({ cause -> _state.update { it.copy(error = cause) } }) { path ->
                _state.update { it.copy(nameImage = path.lastPathSegment(), type = pokeType, pathImage = path) }
            }
        }
    }

    fun onPictureReady(result: Boolean) {
        viewModelScope.launch {
            if (result) {
                val cause = saveCollectionUseCase(pokemonID, _state.value.type!!.id, _state.value.nameImage!!)
                _state.update { it.copy(error = cause) }
                onTakePictureDone()
            } else {
                val cause = deletePhotoUseCase(_state.value.nameImage!!)
                _state.update { it.copy(error = cause) }
                onTakePictureDone()
            }
        }
    }

    fun onUriDone() {
        viewModelScope.launch {
            _state.update { it.copy(pathImage = null) }
        }
    }

    private fun onTakePictureDone() {
        _state.update { it.copy(nameImage = null, type = null) }
    }

    data class UIState(
        val colletionList: List<GalleryItem>? = null,
        val nameImage: String? = null,
        val type: PokeCollec? = null,
        val pathImage: String? = null,
        val error: Error? = null
    )
}