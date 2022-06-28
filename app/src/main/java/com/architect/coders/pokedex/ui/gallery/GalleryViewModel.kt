package com.architect.coders.pokedex.ui.gallery

import android.net.Uri
import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.ui.common.PokeCollec
import com.architect.coders.pokedex.ui.common.toGalleryItem
import com.architect.coders.pokedex.data.database.CollectionL
import com.architect.coders.pokedex.data.toError
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.ui.common.getCollection
import com.architect.coders.pokedex.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val pokemonID: Int,
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
            findCollectionsUseCase(pokemonID)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { collectionList ->
                    _state.update { it.copy(colletionList = collectionList.toGalleryItem(getPathUseCase())) }
            }
        }
    }

    fun onCreatePictureFile(@IdRes fabID: Int) {
        val pokeType = getCollection(fabID)
        val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
        val imageData = createPhotoUseCase(nameFile)
        imageData.fold({ cause -> _state.update { it.copy(error = cause) } }) { uri ->
            _state.update { it.copy(nameImage = uri.lastPathSegment, type = pokeType, uriImage = uri) }
        }
    }

    fun onPictureReady(result: Boolean) {
        if (result) {
            viewModelScope.launch {
                val cause = saveCollectionUseCase(
                    CollectionL(0, pokemonID, _state.value.type!!.id, _state.value.nameImage!!))
                _state.update { it.copy(error = cause) }
                onTakePictureDone()
            }
        } else {
            val cause = deletePhotoUseCase(_state.value.nameImage!!)
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
    private val findCollectionsUseCase: FindCollectionsUseCase,
    private val saveCollectionUseCase: SaveCollectionUseCase,
    private val getPathUseCase: GetPathUseCase,
    private val createPhotoUseCase: CreatePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(
            pokemonID,
            findCollectionsUseCase,
            saveCollectionUseCase,
            getPathUseCase,
            createPhotoUseCase,
            deletePhotoUseCase
        ) as T
    }
}