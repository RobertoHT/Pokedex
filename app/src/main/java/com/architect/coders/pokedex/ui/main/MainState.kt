package com.architect.coders.pokedex.ui.main

import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.architect.coders.pokedex.database.PokemonL

fun Fragment.buildMainState(
    navController: NavController = findNavController()
) = MainState(navController)

class MainState(
    private val navController: NavController
) {

    fun onPokemonClicked(pokemon: PokemonL, @ColorInt color: Int) {
        val action = MainFragmentDirections.actionMainToDetail(pokemon.id, color)
        navController.navigate(action)
    }
}