package com.architect.coders.pokedex.ui.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.databinding.ActivityGalleryBinding
import com.architect.coders.pokedex.file.PokemonPhotoFile
import com.architect.coders.pokedex.ui.collection.CollectionActivity
import com.architect.coders.pokedex.ui.detail.DetailActivity
import com.architect.coders.pokedex.util.getCollection
import com.architect.coders.pokedex.util.getGalleryItems
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity() {

    private val pokemonPhoto = PokemonPhotoFile(this)
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter : GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonID = intent.getIntExtra(DetailActivity.EXTRA_ID, 0)

        adapter = GalleryAdapter(getGalleryItems()) { image -> navigateTo(image) }
        binding.amiiboRecycler.adapter = adapter

        binding.expandableFab.portraitConfiguration.fabOptions.forEach {
                fab -> fab.setOnClickListener { dispatchTakePictureIntent(it.id, pokemonID) }
        }
    }

    private fun dispatchTakePictureIntent(@IdRes fabID: Int, pokemonID: Int) {
        val pokeType = getCollection(fabID)
        lifecycleScope.launch {
            val nameFile = "Poke_${pokemonID}_${pokeType.id}_"
            val pair = pokemonPhoto.takePhoto(nameFile)
            pair?.second?.let { adapter.addImage(pokeType, it) }
        }
    }

    private fun navigateTo(imageSrc: String) {
        val intent = Intent(this, CollectionActivity::class.java)
        intent.putExtra(CollectionActivity.EXTRA_IMAGE, imageSrc)
        startActivity(intent)
    }
}