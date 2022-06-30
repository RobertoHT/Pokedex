package com.architect.coders.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.TypeItemBinding
import com.architect.coders.pokedex.domain.Type
import com.architect.coders.pokedex.ui.common.getTypePokemonColor

class TypeAdapter(private val types: List<Type>) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(types[position])
    }

    override fun getItemCount(): Int = types.size

    class ViewHolder(private val binding: TypeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: Type) {
            binding.typePoke.text = type.name
            binding.typeCardView.setCardBackgroundColor(itemView.context.getColor(
                getTypePokemonColor(type.name)
            ))
        }
    }
}