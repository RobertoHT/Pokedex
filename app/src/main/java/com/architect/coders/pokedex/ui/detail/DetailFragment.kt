package com.architect.coders.pokedex.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.database.PokemonDetailL
import com.architect.coders.pokedex.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels { DetailViewModelFactory(
        PokemonRepository(requireActivity().app),
        safeArgs.id,
        safeArgs.colorSwatch)
    }

    private lateinit var detailState: DetailState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailState = buildDetailState()

        val binding = FragmentDetailBinding.bind(view).apply {
            btnFavorite.setOnClickListener {
                viewModel.onFavoriteClicked()
            }

            btnMyCollection.setOnClickListener {
                detailState.goToTheGallery(viewModel.pokemonID)
            }
        }

        with(viewModel.state) {
            launchCollectAndDiff(this, {it.loading}) { binding.progress.visible = it }
            launchCollectAndDiff(this, {it.views}) {
                binding.cardView.visible = it
                binding.imageDetail.visible = it
                binding.btnFavorite.visible = it
            }
            launchCollectAndDiff(this, {it.colorSwatch}) {
                requireActivity().window.statusBarColor = it
                binding.containerDetail.setBackgroundColor(it)
            }
            launchCollectAndDiff(this, {it.pokemon}) { it?.let { detail -> binding.setupDataInViews(detail) } }
        }
    }

    private fun FragmentDetailBinding.setupDataInViews(detail: PokemonDetailL) {
        nameDetail.text = detail.pokemon.name
        weightDetail.text = getString(R.string.detail_weight, detail.pokemon.weight)
        heightDetail.text = getString(R.string.detail_height, detail.pokemon.height)

        imageDetail.loadWithPath(detail.pokemon.imageUrl())

        val idDrawable = if (detail.pokemon.favorite) R.drawable.ic_favorite_bold else R.drawable.ic_favorite
        btnFavorite.setImageResource(idDrawable)

        val adapterType = TypeAdapter(detail.types)
        typeRecyclerView.adapter = adapterType

        val adapterStat = StatAdapter(detail.stats)
        statRecyclerView.adapter = adapterStat
    }
}