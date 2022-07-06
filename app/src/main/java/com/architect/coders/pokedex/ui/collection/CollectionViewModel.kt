package com.architect.coders.pokedex.ui.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val image = CollectionFragmentArgs.fromSavedStateHandle(savedStateHandle).imagePath

    private val _state = MutableStateFlow(UIState())
    val state : StateFlow<UIState> = _state.asStateFlow()

    init {
        _state.value = UIState(image)
    }

    class UIState(
        val image: String? = null
    )
}