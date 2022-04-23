package com.architect.coders.pokedex.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.GalleryItemBinding
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.common.setCollectionTitle

class GalleryAdapter(private val galleryList: List<GalleryItem>, private val collectionClickListener: (String) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(galleryList[position], collectionClickListener)
    }

    override fun getItemCount(): Int = galleryList.size

    fun addImage(type: PokeCollec, image: String) {
        val index = galleryList.indexOfFirst { it.type == type }
        val item = galleryList[index]

        item.photos.add(image)
        notifyItemChanged(index)
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