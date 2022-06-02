package com.architect.coders.pokedex.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CollectionViewModel(private val image: String) : ViewModel() {

    private val _state = MutableLiveData(UIState())
    val state : LiveData<UIState>
        get() {
            if (_state.value?.image == null) {
                _state.value = UIState(image)
            }

            return _state
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