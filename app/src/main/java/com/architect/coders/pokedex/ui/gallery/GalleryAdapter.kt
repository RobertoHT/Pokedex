package com.architect.coders.pokedex.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.databinding.GalleryItemBinding
import com.bumptech.glide.Glide

class GalleryAdapter(private val imageList: List<String>, val collectionClickListener: () -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
        holder.itemView.setOnClickListener { collectionClickListener() }
    }

    override fun getItemCount(): Int = imageList.size

    class ViewHolder(private val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            Glide.with(binding.root.context).load(image).into(binding.galleryImage)
        }
    }
}