package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.PokemonDetailL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
            pokemonRepository.checkPokemonDetail(pokemonID)

            pokemonRepository.getPokemonDetail(pokemonID).collect { pokemonDetail ->
                _state.value = UIState(pokemon = pokemonDetail, colorSwatch = colorSwatch, views = true)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.pokemon?.let { pokemonRepository.switchFavorite(it.pokemon) }
        }
    }

    data class UIState(
        val pokemon: PokemonDetailL? = null,
        val colorSwatch: Int = 0,
        val loading: Boolean = false,
        val views: Boolean = false
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