package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PokemonRepository

class CheckPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int): Error? = repository.checkPokemonDetail(id)
}