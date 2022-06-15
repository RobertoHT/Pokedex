package com.architect.coders.pokedex.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.launchCollectAndDiff
import com.architect.coders.pokedex.databinding.FragmentGalleryBinding
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.util.getGalleryItems

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val safeArgs: GalleryFragmentArgs by navArgs()
    private val viewModel: GalleryViewModel by viewModels { GalleryViewModelFactory(
        safeArgs.id,
        PokemonPhotoFile(requireActivity().application))
    }

    private lateinit var galleryState: GalleryState
    private lateinit var adapter : GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryState = buildGalleryState()

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.teal_700)

        FragmentGalleryBinding.bind(view).apply {
            adapter = GalleryAdapter(getGalleryItems()) { image -> galleryState.onImageClicked(image) }
            amiiboRecycler.adapter = adapter

            expandableFab.portraitConfiguration.fabOptions.forEach {
                    fab -> fab.setOnClickListener { viewModel.onCreatePictureFile(it.id) }
            }
        }

        with(viewModel.state) {
            launchCollectAndDiff(this, {it.photo}) { pair ->
                pair?.let {
                    adapter.addImage(it.first, it.second)
                    viewModel.onTakePictureDone()
                }
            }
            launchCollectAndDiff(this, {it.uriImage}) { uri ->
                uri?.let {
                    galleryState.onTakePicture(it) { result ->
                        viewModel.onPictureReady(result)
                    }
                    viewModel.onUriDone()
                }
            }
        }
    }
}