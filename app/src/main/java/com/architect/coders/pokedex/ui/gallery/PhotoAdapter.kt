package com.architect.coders.pokedex.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.PhotoItemBinding
import com.architect.coders.pokedex.util.basicDiffUtil
import com.architect.coders.pokedex.util.loadWithPath

class PhotoAdapter(val collectionClickListener: (String) -> Unit) :
    ListAdapter<String, PhotoAdapter.ViewHolder>(basicDiffUtil { old, new -> old == new }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { collectionClickListener(item) }
    }

    class ViewHolder(private val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: String) {
            binding.photoImage.loadWithPath(path)
        }
    }
}