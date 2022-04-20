package com.architect.coders.pokedex.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.imageUrl
import com.architect.coders.pokedex.databinding.ActivityDetailBinding
import com.architect.coders.pokedex.ui.gallery.GalleryActivity
import com.architect.coders.pokedex.common.loadWithPath
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonDetail
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "pokemon:id"
        const val EXTRA_IMAGE_COLOR = "pokemon:colorSwatch"
    }

    private val pokemonRepository by lazy { PokemonRepository() }
    private lateinit var binding: ActivityDetailBinding
    private var favorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonID = intent.getIntExtra(EXTRA_ID, 0)
        val colorSwatch = intent.getIntExtra(EXTRA_IMAGE_COLOR, 0)

        window.statusBarColor = colorSwatch
        visibleViews(false)

        lifecycleScope.launch {
            setupDataInViews(pokemonRepository.getPokemonDetail(pokemonID), colorSwatch)
            visibleViews(true)
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

        binding.btnMyCollection.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, pokemonID)
            startActivity(intent)
        }
    }

    private fun setupDataInViews(pokemon: PokemonDetail, colorSwatch: Int) {
        binding.nameDetail.text = pokemon.name
        binding.weightDetail.text = getString(R.string.detail_weight, pokemon.weight)
        binding.heightDetail.text = getString(R.string.detail_height, pokemon.height)

        binding.imageDetail.loadWithPath(pokemon.imageUrl())
        binding.containerDetail.setBackgroundColor(colorSwatch)

        val adapterType = TypeAdapter(pokemon.types)
        binding.typeRecyclerView.adapter = adapterType

        val adapterStat = StatAdapter(pokemon.stats)
        binding.statRecyclerView.adapter = adapterStat
    }

    private fun visibleViews(isVisible: Boolean) {
        binding.progress.visibility = if (!isVisible) View.VISIBLE else View.GONE
        binding.cardView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.imageDetail.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}