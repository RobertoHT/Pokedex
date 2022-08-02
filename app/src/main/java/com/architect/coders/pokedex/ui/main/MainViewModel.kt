package com.architect.coders.pokedex.ui.main

import androidx.lifecycle.*
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.framework.toError
import com.architect.coders.pokedex.usecases.GetPokemonUseCase
import com.architect.coders.pokedex.usecases.RequestPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val requestPokemonUseCase: RequestPokemonUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getPokemonUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemonList ->
                    if (pokemonList.isNotEmpty()) {
                        _state.update { UIState(pokemonList = pokemonList) }
                    }
                }
        }
    }

    fun validateData() {
        viewModelScope.launch {
            if (state.value.pokemonList.isNullOrEmpty()) {
                _state.update { it.copy(loading = true) }
                notifyLastVisible(0)
            }
        }
    }

    suspend fun notifyLastVisible(lastVisible: Int) {
        val cause = requestPokemonUseCase(lastVisible)
        _state.update { it.copy(error = cause, loading = false) }
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<Pokemon>? = null,
        val error: Error? = null
    )
}