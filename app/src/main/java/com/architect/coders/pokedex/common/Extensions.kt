package com.architect.coders.pokedex.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import com.architect.coders.pokedex.App
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.model.PokemonDetail
import com.architect.coders.pokedex.model.PokemonItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val URL_SPRITE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%d.png"

val Context.app: App
    get() = applicationContext as App

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun PokemonItem.id() : Int {
    val split = url.split("/")
    return split[split.size - 2].toInt()
}

fun PokemonItem.imageUrl() : String =
    String.format(URL_SPRITE, id())

fun PokemonDetail.imageUrl() : String =
    String.format(URL_SPRITE, id)

fun TextView.setCollectionTitle(type: PokeCollec) {
    val title = when(type) {
        PokeCollec.AMIIBO -> context.getString(R.string.gallery_amiibo)
        PokeCollec.PLUSH -> context.getString(R.string.gallery_plush)
        PokeCollec.OTHER -> context.getString(R.string.gallery_other)
    }

    text = title
}

fun ImageView.loadWithPath(path: String) {
    Glide.with(this)
        .load(path)
        .placeholder(R.drawable.ic_loading)
        .error(R.drawable.ic_placeholder)
        .into(this)
}

fun ImageView.loadWithPathWithoutPlaceHolder(path: String) {
    Glide.with(this)
        .load(path)
        .error(R.drawable.ic_placeholder)
        .into(this)
}

fun ImageView.loadWithPathAndGetColor(path: String,  listener: (color: Int) -> Unit) {
    Glide.with(this)
        .load(path)
        .placeholder(R.drawable.ic_loading)
        .error(R.drawable.ic_placeholder)
        .listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                listener(R.color.white)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let { it ->
                    val palette = Palette.from(it.toBitmap()).generate()
                    val swatch = palette.dominantSwatch
                    swatch?.rgb?.let {
                        listener(it)
                    }
                }
                return false
            }

        })
        .into(this)
}

inline fun <T> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}

fun <T, U> LifecycleOwner.launchCollectAndDiff(
    flow: Flow<T>,
    mapf: (T) -> U,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (U) -> Unit
) {
    lifecycleScope.launch {
        this@launchCollectAndDiff.repeatOnLifecycle(state) {
            flow.map(mapf).distinctUntilChanged().collect(body)
        }
    }
}