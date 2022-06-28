package com.architect.coders.pokedex.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.ui.common.*
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.databinding.FragmentMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(PokemonRepository(requireActivity().app)) }
    private val adapter = PokemonAdapter { pokemon, color -> mainState.onPokemonClicked(pokemon, color) }

    private lateinit var mainState: MainState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainState = buildMainState()

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.teal_700)

        val binding = FragmentMainBinding.bind(view).apply {
            recycler.adapter = adapter
        }

        with(viewModel.state) {
            launchCollectAndDiff(this, {it.loading}) { binding.progress.visible = it }
            launchCollectAndDiff(this, {it.pokemonList}) { adapter.submitList(it) }
            launchCollectAndDiff(this, {it.error}) { it?.let {
                showSnackbar(binding.containerMain, requireContext().errorToString(it)) } }
        }

        viewLifecycleOwner.lifecycleScope.collectFlow(binding.recycler.lastVisibleEvents) {
            viewModel.notifyLastVisible(it)
        }
    }
}