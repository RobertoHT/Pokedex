package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.data.database.PokemonL

class SwitchPokemonFavoriteUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemon: PokemonL): Error? = repository.switchFavorite(pokemon)
}