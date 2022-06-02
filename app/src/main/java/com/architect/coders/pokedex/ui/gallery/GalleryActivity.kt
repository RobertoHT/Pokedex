package com.architect.coders.pokedex.ui.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.architect.coders.pokedex.databinding.ActivityGalleryBinding
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.ui.collection.CollectionActivity
import com.architect.coders.pokedex.ui.detail.DetailActivity
import com.architect.coders.pokedex.util.getGalleryItems

class GalleryActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModels { GalleryViewModelFactory(
        intent.getIntExtra(DetailActivity.EXTRA_ID, 0),
        PokemonPhotoFile(this))
    }
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter : GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this, ::addImageToList)

        adapter = GalleryAdapter(getGalleryItems()) { image -> navigateTo(image) }
        binding.amiiboRecycler.adapter = adapter

        binding.expandableFab.portraitConfiguration.fabOptions.forEach {
                fab -> fab.setOnClickListener { viewModel.takePicture(it.id) }
        }
    }

    private fun addImageToList(state: GalleryViewModel.UIState) {
        state.photo?.let {
            adapter.addImage(it.first, it.second)
        }
    }

    private fun navigateTo(imageSrc: String) {
        val intent = Intent(this, CollectionActivity::class.java)
        intent.putExtra(CollectionActivity.EXTRA_IMAGE, imageSrc)
        startActivity(intent)
    }
}