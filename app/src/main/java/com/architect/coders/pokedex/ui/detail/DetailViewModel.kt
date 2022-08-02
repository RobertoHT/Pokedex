package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.*
import com.architect.coders.pokedex.di.ColorSwatch
import com.architect.coders.pokedex.di.PokemonId
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.framework.toError
import com.architect.coders.pokedex.usecases.CheckPokemonUseCase
import com.architect.coders.pokedex.usecases.FindPokemonUseCase
import com.architect.coders.pokedex.usecases.SwitchPokemonFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @PokemonId val pokemonID: Int,
    @ColorSwatch private val colorSwatch: Int,
    private val checkPokemonUseCase: CheckPokemonUseCase,
    private val findPokemonUseCase: FindPokemonUseCase,
    private val switchPokemonFavoriteUseCase: SwitchPokemonFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = UIState(loading = true)

            val cause = checkPokemonUseCase(pokemonID)
            _state.update { it.copy(error = cause) }

            findPokemonUseCase(pokemonID)
                .catch { error -> _state.update { it.copy(error = error.toError()) } }
                .collect { updateState(it) }
        }
    }

    private fun updateState(detail: Pokemon?) {
        if (detail != null) {
            _state.update { UIState(pokemon = detail, colorSwatch = colorSwatch, views = true) }
        } else {
            _state.update { UIState(colorSwatch = colorSwatch) }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.pokemon?.let { detail ->
                val cause = switchPokemonFavoriteUseCase(detail)
                _state.update { it.copy(error = cause) }
            }
        }
    }

    data class UIState(
        val pokemon: Pokemon? = null,
        val colorSwatch: Int = 0,
        val loading: Boolean = false,
        val views: Boolean = false,
        val error: Error? = null
    )
}