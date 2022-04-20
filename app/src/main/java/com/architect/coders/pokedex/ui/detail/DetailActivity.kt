package com.architect.coders.pokedex.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.databinding.ActivityDetailBinding
import com.architect.coders.pokedex.network.PokeClient
import com.architect.coders.pokedex.ui.gallery.GalleryActivity
import com.architect.coders.pokedex.common.loadWithPath
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "pokemon:id"
        const val EXTRA_IMAGE_URL = "pokemon:imageurl"
        const val EXTRA_IMAGE_COLOR = "pokemon:colorSwatch"
    }

    private lateinit var binding: ActivityDetailBinding
    private var favorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonID = intent.getIntExtra(EXTRA_ID, 0)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val colorSwatch = intent.getIntExtra(EXTRA_IMAGE_COLOR, 0)

        window.statusBarColor = colorSwatch

        if (imageUrl != null) {
            visibleViews(false)

            binding.imageDetail.loadWithPath(imageUrl)

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

                visibleViews(true)
            }
        }

        binding.btnMyCollection.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, pokemonID)
            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            val idDrawable = if (favorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_bold
            }
            binding.btnFavorite.setImageResource(idDrawable)
            favorite = !favorite
        }
    }

    private fun visibleViews(isVisible: Boolean) {
        binding.progress.visibility = if (!isVisible) View.VISIBLE else View.GONE
        binding.cardView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.imageDetail.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}