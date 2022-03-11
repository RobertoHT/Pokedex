package com.architect.coders.pokedex.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.databinding.ActivityDetailBinding
import com.architect.coders.pokedex.network.PokeClient
import com.architect.coders.pokedex.ui.gallery.GalleryActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "pokemon:id"
        const val EXTRA_IMAGE_URL = "pokemon:imageurl"
        const val EXTRA_IMAGE_COLOR = "pokemon:colorSwatch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonID = intent.getIntExtra(EXTRA_ID, 0)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val colorSwatch = intent.getIntExtra(EXTRA_IMAGE_COLOR, 0)

        window.statusBarColor = colorSwatch

        if (imageUrl != null) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.imageDetail)

            binding.containerDetail.setBackgroundColor(colorSwatch)

            lifecycleScope.launch {
                val result = PokeClient.service.getPokemonDetail(pokemonID)
                binding.nameDetail.text = result.name
                binding.weightDetail.text = getString(R.string.detail_weight, result.weight)
                binding.heightDetail.text = getString(R.string.detail_height, result.height)

                val adapterType = TypeAdapter(result.types)
                binding.typeRecyclerView.adapter = adapterType

                val adapterStat = StatAdapter(result.stats)
                binding.statRecyclerView.adapter = adapterStat
            }
        }

        binding.btnMyCollection.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, pokemonID)
            startActivity(intent)
        }
    }
}