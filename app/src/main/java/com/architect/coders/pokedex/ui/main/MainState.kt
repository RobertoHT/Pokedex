package com.architect.coders.pokedex.ui.main

import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.architect.coders.pokedex.common.id
import com.architect.coders.pokedex.model.PokemonItem

fun Fragment.buildMainState(
    navController: NavController = findNavController()
) = MainState(navController)

class MainState(
    private val navController: NavController,
) {
    private var loading = true

    fun isLoadMorePokemon(dy: Int, recyclerView: RecyclerView, isLoad: (Boolean) -> Unit) {
        if (dy > 0 && loading && !recyclerView.canScrollVertically(1)) {
            loading = false
            isLoad(true)
        }
    }

    fun enabledLoadMore() {
        loading = true
    }

    fun onPokemonClicked(pokemon: PokemonItem, @ColorInt color: Int) {
        val action = MainFragmentDirections.actionMainToDetail(pokemon.id(), color)
        navController.navigate(action)
    }
}