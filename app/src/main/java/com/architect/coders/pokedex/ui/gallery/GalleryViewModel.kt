package com.architect.coders.pokedex.ui.gallery

import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.util.getCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val pokemonID: Int,
    private val pokemonFile : PokemonPhotoFile
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    fun takePicture(@IdRes fabID: Int) {
        viewModelScope.launch {
            val pokeType = getCollection(fabID)
            val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
            val photoPath = pokemonFile.takePhoto(nameFile)
            photoPath?.let {
                _state.value = UIState(Pair(pokeType, it))
            }
        }
    }

    class UIState(
        val photo : Pair<PokeCollec, String>? = null
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