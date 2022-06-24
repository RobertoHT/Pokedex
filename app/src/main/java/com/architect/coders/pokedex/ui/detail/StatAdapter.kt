package com.architect.coders.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.database.StatL
import com.architect.coders.pokedex.databinding.StatItemBinding
import com.architect.coders.pokedex.util.getStatPokemonColor
import com.architect.coders.pokedex.util.getStatPokemonText

class StatAdapter(private val stats: List<StatL>) : RecyclerView.Adapter<StatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stats[position])
    }

    override fun getItemCount(): Int = stats.size

    class ViewHolder(private val binding: StatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stat: StatL) {
            binding.statTitle.text = getStatPokemonText(stat.name)
            binding.statProgress.progress = stat.baseStat.toFloat()
            binding.statProgress.labelText = stat.baseStat.toString()
            binding.statProgress.highlightView.color = itemView.context.getColor(
                getStatPokemonColor(stat.name)
            )
        }
    }
}