package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.data.database.CollectionL
import kotlinx.coroutines.flow.Flow

class FindCollectionsUseCase(private val repository: PokemonRepository) {
    operator fun invoke(id: Int): Flow<List<CollectionL>> = repository.getCollectionByPokemon(id)
}