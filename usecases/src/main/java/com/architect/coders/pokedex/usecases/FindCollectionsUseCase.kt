package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.GalleryItem
import kotlinx.coroutines.flow.Flow

class FindCollectionsUseCase(private val repository: PokemonRepository) {
    operator fun invoke(id: Int, path: String): Flow<List<GalleryItem>> = repository.getCollectionByPokemon(id, path)
}