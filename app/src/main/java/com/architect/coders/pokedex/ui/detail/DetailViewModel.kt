package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.network.PokemonDetailR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val pokemonRepository: PokemonRepository,
    val pokemonID: Int,
    private val colorSwatch: Int
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    private var favorite: Boolean = false

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UIState(loading = true)
            _state.value = UIState(pokemon = pokemonRepository.getPokemonDetail(pokemonID), colorSwatch = colorSwatch, views = true)
        }
    }

    fun favoriteClicked() {
        favorite = !favorite
        _state.value = _state.value.copy(favorite = favorite)
    }

    data class UIState(
        val pokemon: PokemonDetailR? = null,
        val colorSwatch: Int = 0,
        val loading: Boolean = false,
        val views: Boolean = false,
        val favorite: Boolean = false
    )
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val pokemoRepository: PokemonRepository,
    private val pokemonID: Int,
    private val colorSwatch: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(pokemoRepository ,pokemonID, colorSwatch) as T
    }
}