package com.architect.coders.pokedex.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.databinding.FragmentGalleryBinding
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.util.getGalleryItems
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val safeArgs: GalleryFragmentArgs by navArgs()
    private val viewModel: GalleryViewModel by viewModels { GalleryViewModelFactory(
        safeArgs.id,
        PokemonPhotoFile(requireActivity() as AppCompatActivity))
    }
    private lateinit var adapter : GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentGalleryBinding.bind(view).apply {
            adapter = GalleryAdapter(getGalleryItems()) { image -> navigateTo(image) }
            amiiboRecycler.adapter = adapter

            expandableFab.portraitConfiguration.fabOptions.forEach {
                    fab -> fab.setOnClickListener { viewModel.takePicture(it.id) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::addImageToList)
            }
        }
    }

    private fun addImageToList(state: GalleryViewModel.UIState) {
        state.photo?.let {
            adapter.addImage(it.first, it.second)
        }
    }

    private fun navigateTo(imageSrc: String) {
        val action = GalleryFragmentDirections.actionGalleryToCollection(imageSrc)
        findNavController().navigate(action)
    }
}