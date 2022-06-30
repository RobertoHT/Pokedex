package com.architect.coders.pokedex.ui.common

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.App
import com.architect.coders.pokedex.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.PokeCollec
import com.architect.coders.pokedex.domain.Pokemon

private const val URL_SPRITE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%d.png"

val Context.app: App
    get() = applicationContext as App

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun Pokemon.imageUrl() : String =
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
                resource?.let { bitmap ->
                    val palette = Palette.from(bitmap.toBitmap()).generate()
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

fun <T> CoroutineScope.collectFlow(flow: Flow<T>, body: suspend (T) -> Unit) {
    flow.onEach { body(it) }
        .launchIn(this)
}

@ExperimentalCoroutinesApi
val RecyclerView.lastVisibleEvents: Flow<Int>
    get() = callbackFlow<Int> {
        val lm = layoutManager as GridLayoutManager

        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                trySend(lm.findLastVisibleItemPosition()).isSuccess
            }
        }
        addOnScrollListener(listener)
        awaitClose { removeOnScrollListener(listener) }
    }.conflate()

fun Int.getTypeById(): PokeCollec =
    when(this) {
        1 -> PokeCollec.AMIIBO
        2 -> PokeCollec.PLUSH
        else -> PokeCollec.OTHER
    }

fun Context.errorToString(error: Error): String = when (error) {
    Error.Connectivity -> getString(R.string.error_conectivity)
    Error.Server -> getString(R.string.error_server)
    Error.File -> getString(R.string.error_file)
    Error.Unknown -> getString(R.string.error_unknown)
}

fun showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun String.lastPathSegment(): String =
    this.split("/").last()