package com.architect.coders.pokedex.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.ActivityMainBinding
import com.architect.coders.pokedex.common.id
import com.architect.coders.pokedex.common.visible
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonItem
import com.architect.coders.pokedex.ui.detail.DetailActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(PokemonRepository()) }
    private val adapter = PokemonAdapter { pokemon, color -> viewModel.onPokemonClicked(pokemon, color) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = adapter
        setupScrollListener(binding.recycler)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateUI)
            }
        }
    }

    private fun setupScrollListener(reycler: RecyclerView) {
        reycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.scrolled(dy, recyclerView.canScrollVertically(1))
            }
        })
    }

    private fun updateUI(state: MainViewModel.UIState) {
        binding.progress.visible = state.loading
        state.pokemonList?.let(adapter::submitList)
        state.navigateTo?.let { navigateTo(it.first, it.second) }
    }

    private fun navigateTo(pokemon: PokemonItem, @ColorInt color: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, pokemon.id())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_COLOR, color)
        startActivity(intent)
    }
}