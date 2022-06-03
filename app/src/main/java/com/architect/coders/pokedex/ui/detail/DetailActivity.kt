package com.architect.coders.pokedex.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.imageUrl
import com.architect.coders.pokedex.databinding.ActivityDetailBinding
import com.architect.coders.pokedex.ui.gallery.GalleryActivity
import com.architect.coders.pokedex.common.loadWithPath
import com.architect.coders.pokedex.common.visible
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonDetail
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "pokemon:id"
        const val EXTRA_IMAGE_COLOR = "pokemon:colorSwatch"
    }

    private val viewModel: DetailViewModel by viewModels { DetailViewModelFactory(
        PokemonRepository(),
        intent.getIntExtra(EXTRA_ID, 0),
        intent.getIntExtra(EXTRA_IMAGE_COLOR, 0))
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateUI)
            }
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.favoriteClicked()
        }

        binding.btnMyCollection.setOnClickListener {
            viewModel.collectionClicked()
        }
    }

    private fun updateUI(state: DetailViewModel.UIState) {
        binding.progress.visible = state.loading

        state.views.let {
            binding.cardView.visible = it
            binding.imageDetail.visible = it
        }

        state.colorSwatch.let {
            window.statusBarColor = it
            binding.containerDetail.setBackgroundColor(it)
        }

        state.pokemon?.let { setupDataInViews(it) }
        state.navigateTo?.let { navigateTo(it) }

        state.favorite.let {
            val idDrawable = if (it) R.drawable.ic_favorite_bold else R.drawable.ic_favorite
            binding.btnFavorite.setImageResource(idDrawable)
        }
    }

    private fun setupDataInViews(pokemon: PokemonDetail) {
        binding.nameDetail.text = pokemon.name
        binding.weightDetail.text = getString(R.string.detail_weight, pokemon.weight)
        binding.heightDetail.text = getString(R.string.detail_height, pokemon.height)

        binding.imageDetail.loadWithPath(pokemon.imageUrl())

        val adapterType = TypeAdapter(pokemon.types)
        binding.typeRecyclerView.adapter = adapterType

        val adapterStat = StatAdapter(pokemon.stats)
        binding.statRecyclerView.adapter = adapterStat
    }

    private fun navigateTo(pokemonID: Int) {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra(EXTRA_ID, pokemonID)
        startActivity(intent)
    }
}