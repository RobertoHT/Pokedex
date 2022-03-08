package com.architect.coders.pokedex.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.databinding.PokemonItemBinding
import com.architect.coders.pokedex.imageUrl
import com.architect.coders.pokedex.model.PokemonItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class PokemonAdapter(private val pokemonList: List<PokemonItem>, private val pokemonClickListener: (PokemonItem, colorSwatch: Int) -> Unit)
    : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, pokemonClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount(): Int = pokemonList.size

    class ViewHolder(private val binding: PokemonItemBinding, private val pokemonClickListener: (PokemonItem, colorSwatch: Int) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        private var colorPokemon = R.color.white

        fun bind(pokemon: PokemonItem) {
            binding.nameItem.text = pokemon.name
            Glide.with(binding.root.context)
                .load(pokemon.imageUrl())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            setColorSwatch(resource.toBitmap())
                        }
                        return false
                    }
                })
                .into(binding.imageItem)

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