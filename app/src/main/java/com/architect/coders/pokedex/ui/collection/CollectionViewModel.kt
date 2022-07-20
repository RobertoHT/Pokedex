package com.architect.coders.pokedex.ui.collection

import androidx.lifecycle.ViewModel
import com.architect.coders.pokedex.di.PokemonImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(@PokemonImage private val image: String) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state : StateFlow<UIState> = _state.asStateFlow()

    init {
        _state.value = UIState(image)
    }

    data class UIState(
        val image: String? = null
    )
}