package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.data.database.PokemonDetailL
import kotlinx.coroutines.flow.Flow

class FindPokemonUseCase(private val repository: PokemonRepository) {
    operator fun invoke(id: Int): Flow<PokemonDetailL> = repository.getPokemonDetail(id)
}