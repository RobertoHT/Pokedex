package com.architect.coders.pokedex.ui.main

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.PokemonL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val pokemoRepository: PokemonRepository) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refresh()
            pokemoRepository.pokemonList.collect { pokemonList ->
                _state.value = UIState(pokemonList = pokemonList)
            }
        }
    }

    private suspend fun refresh() {
        _state.value = UIState(loading = true)
        notifyLastVisible(0)
    }

    suspend fun notifyLastVisible(lastVisible: Int) {
        pokemoRepository.checkRequierePokemonData(lastVisible)
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<PokemonL>? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemoRepository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemoRepository) as T
    }
}