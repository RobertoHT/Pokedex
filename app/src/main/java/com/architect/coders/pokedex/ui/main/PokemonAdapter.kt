package com.architect.coders.pokedex.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.common.*
import com.architect.coders.pokedex.databinding.PokemonItemBinding
import com.architect.coders.pokedex.model.PokemonItem

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
            binding.imageItem.loadWithPathAndGetColor(pokemon.imageUrl()) {
                colorPokemon = it
                binding.cardview.setCardBackgroundColor(it)
            }

            binding.root.setOnClickListener {
                pokemonClickListener(pokemon, colorPokemon)
            }
        }
    }
}