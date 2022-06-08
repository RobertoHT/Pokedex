package com.architect.coders.pokedex.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.imageUrl
import com.architect.coders.pokedex.common.loadWithPath
import com.architect.coders.pokedex.common.visible
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.databinding.FragmentDetailBinding
import com.architect.coders.pokedex.model.PokemonDetail
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels { DetailViewModelFactory(
        PokemonRepository(),
        safeArgs.id,
        safeArgs.colorSwatch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view).apply {
            btnFavorite.setOnClickListener {
                viewModel.favoriteClicked()
            }

            btnMyCollection.setOnClickListener {
                viewModel.collectionClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { binding.updateUI(it) }
            }
        }
    }

    private fun FragmentDetailBinding.updateUI(state: DetailViewModel.UIState) {
        progress.visible = state.loading

        state.views.let {
            cardView.visible = it
            imageDetail.visible = it
        }

        state.colorSwatch.let {
            //window.statusBarColor = it
            containerDetail.setBackgroundColor(it)
        }

        state.pokemon?.let { setupDataInViews(it) }
        state.navigateTo?.let { navigateTo(it) }

        state.favorite.let {
            val idDrawable = if (it) R.drawable.ic_favorite_bold else R.drawable.ic_favorite
            btnFavorite.setImageResource(idDrawable)
        }
    }

    private fun FragmentDetailBinding.setupDataInViews(pokemon: PokemonDetail) {
        nameDetail.text = pokemon.name
        weightDetail.text = getString(R.string.detail_weight, pokemon.weight)
        heightDetail.text = getString(R.string.detail_height, pokemon.height)

        imageDetail.loadWithPath(pokemon.imageUrl())

        val adapterType = TypeAdapter(pokemon.types)
        typeRecyclerView.adapter = adapterType

        val adapterStat = StatAdapter(pokemon.stats)
        statRecyclerView.adapter = adapterStat
    }

    private fun navigateTo(pokemonID: Int) {
        val action = DetailFragmentDirections.actionDetailToGallery(pokemonID)
        findNavController().navigate(action)
    }
}