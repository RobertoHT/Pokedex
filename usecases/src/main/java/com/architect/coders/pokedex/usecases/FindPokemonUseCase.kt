package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(id: Int): Flow<Pokemon> = repository.getPokemonDetail(id)
}