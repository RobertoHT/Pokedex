package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.domain.GalleryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindCollectionsUseCase @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(id: Int, path: String): Flow<List<GalleryItem>> = repository.getCollectionByPokemon(id, path)
}