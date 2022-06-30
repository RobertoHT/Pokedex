package com.architect.coders.pokedex.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.ui.common.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.databinding.FragmentDetailBinding
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.framework.database.PokemonRoomDataSource
import com.architect.coders.pokedex.framework.network.PokemonServerDataSource
import com.architect.coders.pokedex.usecases.CheckPokemonUseCase
import com.architect.coders.pokedex.usecases.FindPokemonUseCase
import com.architect.coders.pokedex.usecases.SwitchPokemonFavoriteUseCase

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels {
        val repository = PokemonRepository(
            PokemonRoomDataSource(requireActivity().app.db.pokemonDao()),
            PokemonServerDataSource()
        )
        DetailViewModelFactory(
            CheckPokemonUseCase(repository),
            FindPokemonUseCase(repository),
            SwitchPokemonFavoriteUseCase(repository),
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
            launchCollectAndDiff(this, {it.error}) { it?.let {
                showSnackbar(binding.containerDetail, requireContext().errorToString(it)) } }
        }
    }

    private fun FragmentDetailBinding.setupDataInViews(detail: Pokemon) {
        nameDetail.text = detail.name
        weightDetail.text = getString(R.string.detail_weight, detail.weight)
        heightDetail.text = getString(R.string.detail_height, detail.height)

        imageDetail.loadWithPath(detail.imageUrl())

        val idDrawable = if (detail.favorite) R.drawable.ic_favorite_bold else R.drawable.ic_favorite
        btnFavorite.setImageResource(idDrawable)

        val adapterType = TypeAdapter(detail.types)
        typeRecyclerView.adapter = adapterType

        val adapterStat = StatAdapter(detail.stats)
        statRecyclerView.adapter = adapterStat
    }
}