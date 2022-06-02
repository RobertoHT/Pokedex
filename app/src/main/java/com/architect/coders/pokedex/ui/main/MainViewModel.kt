package com.architect.coders.pokedex.ui.main

import androidx.annotation.ColorInt
import androidx.lifecycle.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonItem
import kotlinx.coroutines.launch

class MainViewModel(private val pokemoRepository: PokemonRepository) : ViewModel() {

    private val pokemonList : MutableList<PokemonItem> = arrayListOf()
    private var loading = true
    private var offset = 0

    private val _state = MutableLiveData(UIState())
    val state: LiveData<UIState>
        get() {
        if (_state.value?.pokemonList == null) {
            refresh()
        }
        return _state
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UIState(loading = true)
            getPokemonList()
        }
    }

    fun onPokemonClicked(pokemon: PokemonItem, @ColorInt color: Int){
        _state.value = _state.value?.copy(navigateTo = Pair(pokemon, color))
    }

    fun scrolled(dy: Int, canScrollVertically: Boolean) {
        if (dy > 0 && loading && !canScrollVertically) {
            loading = false
            offset += 20
            viewModelScope.launch {
                getPokemonList()
                loading = true
            }
        }
    }

    private suspend fun getPokemonList() {
        pokemonList.addAll(pokemoRepository.getPokemonList(offset).pokemonItems)
        _state.value = UIState(pokemonList = pokemonList.toList())
    }

    data class UIState(
        val loading: Boolean = false,
        val pokemonList: List<PokemonItem>? = null,
        val navigateTo: Pair<PokemonItem, Int>? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemoRepository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemoRepository) as T
    }
}