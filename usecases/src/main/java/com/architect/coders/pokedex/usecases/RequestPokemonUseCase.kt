package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.Error
import javax.inject.Inject

class RequestPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(lastVisible: Int): Error? = repository.checkRequierePokemonData(lastVisible)
}