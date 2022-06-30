package com.architect.coders.pokedex.ui.main

import androidx.lifecycle.*
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.framework.toError
import com.architect.coders.pokedex.usecases.GetPokemonUseCase
import com.architect.coders.pokedex.usecases.RequestPokemonUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val requestPokemonUseCase: RequestPokemonUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refresh()
            getPokemonUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemonList -> _state.update { UIState(pokemonList = pokemonList) } }
        }
    }

    private suspend fun refresh() {
        _state.value = UIState(loading = true)
        notifyLastVisible(0)
    }

    suspend fun notifyLastVisible(lastVisible: Int) {
        val cause = requestPokemonUseCase(lastVisible)
        _state.update { it.copy(error = cause) }
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<Pokemon>? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val requestPokemonUseCase: RequestPokemonUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(getPokemonUseCase, requestPokemonUseCase) as T
    }
}