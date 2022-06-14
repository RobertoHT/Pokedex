package com.architect.coders.pokedex.ui.main

import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val pokemoRepository: PokemonRepository) : ViewModel() {

    private val pokemonList : MutableList<PokemonItem> = arrayListOf()
    private var offset = 0

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        _state.value = UIState(loading = true)
        getPokemonList()
    }

    fun getMorePokemonList() {
        offset += 20
        getPokemonList()
    }

    private fun getPokemonList() {
        viewModelScope.launch {
            pokemonList.addAll(pokemoRepository.getPokemonList(offset).pokemonItems)
            _state.value = UIState(pokemonList = pokemonList.toList())
        }
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<PokemonItem>? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemoRepository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemoRepository) as T
    }
}