package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon

class SwitchPokemonFavoriteUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemon: Pokemon): Error? = repository.switchFavorite(pokemon)
}