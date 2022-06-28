package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PokemonRepository

class RequestPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(lastVisible: Int): Error? = repository.checkRequierePokemonData(lastVisible)
}