package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.data.database.PokemonL
import kotlinx.coroutines.flow.Flow

class GetPokemonUseCase(private val repository: PokemonRepository) {
    operator fun invoke(): Flow<List<PokemonL>> = repository.pokemonList
}