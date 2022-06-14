package com.architect.coders.pokedex.ui.detail

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.buildDetailState(
    navController: NavController = findNavController()
) = DetailState(navController)
class DetailState(
    private val navController: NavController,
) {

    fun goToTheGallery(pokemonID: Int) {
        val action = DetailFragmentDirections.actionDetailToGallery(pokemonID)
        navController.navigate(action)
    }
}