package com.architect.coders.pokedex.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.ActivityMainBinding
import com.architect.coders.pokedex.common.id
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.model.PokemonItem
import com.architect.coders.pokedex.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val adapter = PokemonAdapter { pokemon, color -> navigateTo(pokemon, color) }
    private val pokemonList : MutableList<PokemonItem> = arrayListOf()
    private val pokemonRepository by lazy { PokemonRepository() }

    private var loading = true
    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = adapter
        setupScrollListener(binding.recycler)

        lifecycleScope.launch {
            binding.progress.visibility = View.VISIBLE
            getPokemonList()
            binding.progress.visibility = View.GONE
        }
    }

    private fun setupScrollListener(reycler: RecyclerView) {
        reycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && loading && !recyclerView.canScrollVertically(1)) {
                    loading = false
                    offset += 20
                    lifecycleScope.launch {
                        getPokemonList()
                        loading = true
                    }
                }
            }
        })
    }

    private suspend fun getPokemonList() {
        pokemonList.addAll(pokemonRepository.getPokemonList(offset).pokemonItems)
        adapter.submitList(pokemonList.toList())
    }

    private fun navigateTo(pokemon: PokemonItem, @ColorInt color: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, pokemon.id())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_COLOR, color)
        startActivity(intent)
    }
}