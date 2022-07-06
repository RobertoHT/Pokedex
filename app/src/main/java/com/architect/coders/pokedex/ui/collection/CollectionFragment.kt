package com.architect.coders.pokedex.ui.collection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.ui.common.loadWithPathWithoutPlaceHolder
import com.architect.coders.pokedex.databinding.FragmentCollectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val viewModel: CollectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentCollectionBinding.bind(view).apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collect { photo ->
                        photo.image?.let { imvCollection.loadWithPathWithoutPlaceHolder(it) }
                    }
                }
            }
        }
    }
}