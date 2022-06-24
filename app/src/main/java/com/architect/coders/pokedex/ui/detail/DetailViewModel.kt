package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.PokemonDetailL
import com.architect.coders.pokedex.model.Error
import com.architect.coders.pokedex.model.toError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val pokemonRepository: PokemonRepository,
    val pokemonID: Int,
    private val colorSwatch: Int
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = UIState(loading = true)

            val cause = pokemonRepository.checkPokemonDetail(pokemonID)
            _state.update { it.copy(error = cause) }

            pokemonRepository.getPokemonDetail(pokemonID)
                .catch { error -> _state.update { it.copy(error = error.toError()) } }
                .collect { updateState(it) }
        }
    }

    private fun updateState(detail: PokemonDetailL?) {
        if (detail != null) {
            _state.update { UIState(pokemon = detail, colorSwatch = colorSwatch, views = true) }
        } else {
            _state.update { UIState(colorSwatch = colorSwatch) }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.pokemon?.let { detail ->
                val cause = pokemonRepository.switchFavorite(detail.pokemon)
                _state.update { it.copy(error = cause) }
            }
        }
    }

    data class UIState(
        val pokemon: PokemonDetailL? = null,
        val colorSwatch: Int = 0,
        val loading: Boolean = false,
        val views: Boolean = false,
        val error: Error? = null
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