package com.architect.coders.pokedex.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.lifecycle.lifecycleScope
import com.architect.coders.pokedex.network.PokeClient
import com.architect.coders.pokedex.databinding.ActivityMainBinding
import com.architect.coders.pokedex.id
import com.architect.coders.pokedex.imageUrl
import com.architect.coders.pokedex.model.PokemonItem
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val result = PokeClient.service.getPokemonList(0)
            val adapter = PokemonAdapter(result.pokemonItems) { pokemon, color -> navigateTo(pokemon, color) }
            binding.recycler.adapter = adapter
        }
    }

    private fun navigateTo(pokemon: PokemonItem, @ColorInt color: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, pokemon.id())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, pokemon.imageUrl())
        intent.putExtra(DetailActivity.EXTRA_IMAGE_COLOR, color)
        startActivity(intent)
    }
}