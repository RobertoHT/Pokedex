package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.*
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.framework.toError
import com.architect.coders.pokedex.usecases.CheckPokemonUseCase
import com.architect.coders.pokedex.usecases.FindPokemonUseCase
import com.architect.coders.pokedex.usecases.SwitchPokemonFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val checkPokemonUseCase: CheckPokemonUseCase,
    private val findPokemonUseCase: FindPokemonUseCase,
    private val switchPokemonFavoriteUseCase: SwitchPokemonFavoriteUseCase,
    val pokemonID: Int,
    private val colorSwatch: Int
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

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val checkPokemonUseCase: CheckPokemonUseCase,
    private val findPokemonUseCase: FindPokemonUseCase,
    private val switchPokemonFavoriteUseCase: SwitchPokemonFavoriteUseCase,
    private val pokemonID: Int,
    private val colorSwatch: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(
            checkPokemonUseCase,
            findPokemonUseCase,
            switchPokemonFavoriteUseCase,
            pokemonID,
            colorSwatch
        ) as T
    }
}