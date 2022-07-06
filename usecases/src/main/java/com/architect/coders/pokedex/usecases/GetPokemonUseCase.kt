package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(): Flow<List<Pokemon>> = repository.pokemonList
}