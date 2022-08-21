package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon
import javax.inject.Inject

class SwitchPokemonFavoriteUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemon: Pokemon): Error? = repository.switchFavorite(pokemon)
}