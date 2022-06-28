package com.architect.coders.pokedex.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.ui.common.basicDiffUtil
import com.architect.coders.pokedex.databinding.GalleryItemBinding
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.ui.common.setCollectionTitle

class GalleryAdapter(private val collectionClickListener: (String) -> Unit) :
    ListAdapter<GalleryItem, GalleryAdapter.ViewHolder>(
        basicDiffUtil { old, new -> old.photos.size == new.photos.size }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), collectionClickListener)
    }

    class ViewHolder(private val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewPool = RecyclerView.RecycledViewPool()

        fun bind(item: GalleryItem, clickListener: (String) -> Unit) {
            val adapter = PhotoAdapter(clickListener)
            binding.galleryTitle.setCollectionTitle(item.type)
            binding.galleryRecycler.adapter = adapter
            binding.galleryRecycler.setRecycledViewPool(viewPool)
            adapter.submitList(item.photos)
        }
    }
}