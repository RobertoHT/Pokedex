package com.architect.coders.pokedex.ui.collection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.loadWithPathWithoutPlaceHolder
import com.architect.coders.pokedex.databinding.FragmentCollectionBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val safeArgs: CollectionFragmentArgs by navArgs()
    private val viewModel: CollectionViewModel by viewModels {
        CollectionViewModelFactory(safeArgs.imagePath)
    }

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