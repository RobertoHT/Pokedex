package com.architect.coders.pokedex.ui.main

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.PokemonL
import com.architect.coders.pokedex.model.Error
import com.architect.coders.pokedex.model.toError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val pokemoRepository: PokemonRepository) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refresh()
            pokemoRepository.pokemonList
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemonList -> _state.update { UIState(pokemonList = pokemonList) } }
        }
    }

    private suspend fun refresh() {
        _state.value = UIState(loading = true)
        notifyLastVisible(0)
    }

    suspend fun notifyLastVisible(lastVisible: Int) {
        val cause = pokemoRepository.checkRequierePokemonData(lastVisible)
        _state.update { it.copy(error = cause) }
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<PokemonL>? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemoRepository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemoRepository) as T
    }
}