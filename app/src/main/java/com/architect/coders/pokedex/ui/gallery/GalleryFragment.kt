package com.architect.coders.pokedex.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.ui.common.errorToString
import com.architect.coders.pokedex.ui.common.launchCollectAndDiff
import com.architect.coders.pokedex.ui.common.showSnackbar
import com.architect.coders.pokedex.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var galleryState: GalleryState
    private lateinit var adapter : GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryState = buildGalleryState()

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.teal_700)

        val binding = FragmentGalleryBinding.bind(view).apply {
            adapter = GalleryAdapter { image -> galleryState.onImageClicked(image) }
            collectionRecycler.adapter = adapter

            expandableFab.portraitConfiguration.fabOptions.forEach {
                    fab -> fab.setOnClickListener { viewModel.onCreatePictureFile(it.id) }
            }
        }

        with(viewModel.state) {
            launchCollectAndDiff(this, {it.colletionList}) { list ->
                list?.let { adapter.submitList(it) }
            }
            launchCollectAndDiff(this, {it.pathImage}) { path ->
                path?.let {
                    galleryState.onTakePicture(it) { result ->
                        viewModel.onPictureReady(result)
                    }
                    viewModel.onUriDone()
                }
            }
            launchCollectAndDiff(this, {it.error}) { it?.let {
                showSnackbar(binding.containerGallery, requireContext().errorToString(it))
            } }
        }
    }
}