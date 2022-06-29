package com.architect.coders.pokedex.data

import com.architect.coders.pokedex.data.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.data.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.GalleryItem
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.domain.tryCall
import kotlinx.coroutines.flow.Flow

private const val PAGE_THRESHOLD = 6

class PokemonRepository(
    private val localDataSource: PokemonLocalDataSource,
    private val remoteDataSource: PokemonRemoteDataSource
) {

    val pokemonList = localDataSource.pokemonList

    suspend fun checkRequierePokemonData(lastVisible: Int): Error? = tryCall {
        val size = localDataSource.size()

        if (lastVisible >= size - PAGE_THRESHOLD) {
            val pokemonList = remoteDataSource.getPokemonList(size)
            localDataSource.save(pokemonList)
        }
    }

    fun getPokemonDetail(pokemonID: Int): Flow<Pokemon> = localDataSource.findById(pokemonID)

    suspend fun checkPokemonDetail(pokemonID: Int): Error? = tryCall {
        if (localDataSource.isEmpty(pokemonID)) {
            val pokemonDetail = remoteDataSource.getPokemonDetail(pokemonID)
            savePokemonDetail(pokemonDetail)
        }
    }

    private suspend fun savePokemonDetail(pokemon: Pokemon) {
        val id = pokemon.id
        localDataSource.saveTypes(id, pokemon.types)
        localDataSource.saveStats(id, pokemon.stats)
        localDataSource.update(pokemon)
    }

    suspend fun switchFavorite(pokemon: Pokemon): Error? = tryCall {
        val updatePokemon = pokemon.copy(favorite = !pokemon.favorite)
        localDataSource.update(updatePokemon)
    }

    fun getCollectionByPokemon(pokemonID: Int, path: String): Flow<List<GalleryItem>> =
        localDataSource.getCollectionById(pokemonID, path)

    suspend fun saveCollectionByPokemon(id: Int, type: Int, image: String): Error? = tryCall {
        localDataSource.saveCollection(id, type, image)
    }
}