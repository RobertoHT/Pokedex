package com.architect.coders.pokedex.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CollectionViewModel(image: String) : ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state : StateFlow<UIState> = _state.asStateFlow()

    init {
        _state.value = UIState(image)
    }

    class UIState(
        val image: String? = null
    )
}

@Suppress("UNCHECKED_CAST")
class CollectionViewModelFactory(private val image: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CollectionViewModel(image) as T
    }
}