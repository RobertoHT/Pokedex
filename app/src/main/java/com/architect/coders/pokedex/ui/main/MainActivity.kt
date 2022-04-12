package com.architect.coders.pokedex.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.ActivityMainBinding
import com.architect.coders.pokedex.util.id
import com.architect.coders.pokedex.util.imageUrl
import com.architect.coders.pokedex.model.PokemonItem
import com.architect.coders.pokedex.network.PokeClient
import com.architect.coders.pokedex.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var loading = true
    private var offset = 0
    private val adapter = PokemonAdapter { pokemon, color -> navigateTo(pokemon, color) }
    private val pokemonList : MutableList<PokemonItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMorePokemon(binding.recycler)
        lifecycleScope.launch {
            binding.progress.visibility = View.VISIBLE
            val result = PokeClient.service.getPokemonList(offset)
            pokemonList.addAll(result.pokemonItems)
            binding.recycler.adapter = adapter
            adapter.submitList(pokemonList.toList())
            binding.progress.visibility = View.GONE
        }
    }

    private fun loadMorePokemon(reycler: RecyclerView) {
        reycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && loading && !recyclerView.canScrollVertically(1)) {
                    loading = false
                    offset += 20
                    lifecycleScope.launch {
                        val result = PokeClient.service.getPokemonList(offset)
                        pokemonList.addAll(result.pokemonItems)
                        adapter.submitList(pokemonList.toList())
                        loading = true
                    }
                }
            }
        })
    }

    private fun navigateTo(pokemon: PokemonItem, @ColorInt color: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, pokemon.id())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, pokemon.imageUrl())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_COLOR, color)
        startActivity(intent)
    }
}