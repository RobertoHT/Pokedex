package com.architect.coders.pokedex.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.launchCollectAndDiff
import com.architect.coders.pokedex.common.visible
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(PokemonRepository()) }
    private val adapter = PokemonAdapter { pokemon, color -> mainState.onPokemonClicked(pokemon, color) }

    private lateinit var mainState: MainState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainState = buildMainState()

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.teal_700)

        val binding = FragmentMainBinding.bind(view).apply {
            recycler.adapter = adapter
            setupScrollListener(recycler)
        }

        with(viewModel.state) {
            launchCollectAndDiff(this, {it.loading}) { binding.progress.visible = it }
            launchCollectAndDiff(this, {it.pokemonList}) { adapter.submitList(it) }
        }
    }

    private fun setupScrollListener(reycler: RecyclerView) {
        reycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mainState.isLoadMorePokemon(dy, recyclerView) {
                    viewModel.getMorePokemonList()
                    mainState.enabledLoadMore()
                }
            }
        })
    }
}