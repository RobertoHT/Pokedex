package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.Error

class RequestPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(lastVisible: Int): Error? = repository.checkRequierePokemonData(lastVisible)
}