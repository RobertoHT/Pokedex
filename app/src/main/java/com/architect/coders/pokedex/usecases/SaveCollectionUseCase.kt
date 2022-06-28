package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.Error
import com.architect.coders.pokedex.data.PokemonRepository
import com.architect.coders.pokedex.data.database.CollectionL

class SaveCollectionUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(collection: CollectionL): Error? = repository.saveCollectionByPokemon(collection)
}