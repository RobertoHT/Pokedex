package com.architect.coders.pokedex.ui.main

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.databinding.PokemonItemBinding
import com.architect.coders.pokedex.model.PokemonItem
import com.architect.coders.pokedex.util.basicDiffUtil
import com.architect.coders.pokedex.util.id
import com.architect.coders.pokedex.util.imageUrl
import com.architect.coders.pokedex.util.loadWithPathAndListener

class PokemonAdapter(private val pokemonClickListener: (PokemonItem, colorSwatch: Int) -> Unit) :
    ListAdapter<PokemonItem, PokemonAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id() == new.id() }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, pokemonClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: PokemonItemBinding, private val pokemonClickListener: (PokemonItem, colorSwatch: Int) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        private var colorPokemon = R.color.white

        fun bind(pokemon: PokemonItem) {
            binding.nameItem.text = pokemon.name
            binding.imageItem.loadWithPathAndListener(pokemon.imageUrl()) {
                setColorSwatch(it.toBitmap())
            }

            binding.root.setOnClickListener {
                pokemonClickListener(pokemon, colorPokemon)
            }
        }

        private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

        private fun setColorSwatch(bitmap: Bitmap) {
            val swatch = createPaletteSync(bitmap).dominantSwatch
            val rgb = swatch?.rgb
            if (rgb != null) {
                binding.cardview.setCardBackgroundColor(rgb)
                colorPokemon = rgb
            }
        }
    }
}