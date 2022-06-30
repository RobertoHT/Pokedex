package com.architect.coders.pokedex.data

import com.architect.coders.pokedex.data.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.data.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.GalleryItem
import com.architect.coders.pokedex.domain.Pokemon
import kotlinx.coroutines.flow.Flow

private const val PAGE_THRESHOLD = 6

class PokemonRepository(
    private val localDataSource: PokemonLocalDataSource,
    private val remoteDataSource: PokemonRemoteDataSource
) {

    val pokemonList = localDataSource.pokemonList

    suspend fun checkRequierePokemonData(lastVisible: Int): Error? {
        val size = localDataSource.size()

        if (lastVisible >= size - PAGE_THRESHOLD) {
            val pokemonList = remoteDataSource.getPokemonList(size)
            pokemonList.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }

    fun getPokemonDetail(pokemonID: Int): Flow<Pokemon> = localDataSource.findById(pokemonID)

    suspend fun checkPokemonDetail(pokemonID: Int): Error? {
        if (localDataSource.isEmpty(pokemonID)) {
            val pokemonDetail = remoteDataSource.getPokemonDetail(pokemonID)
            pokemonDetail.fold(ifLeft = { return it }) {
                val id = it.id
                localDataSource.saveTypes(id, it.types)
                localDataSource.saveStats(id, it.stats)
                localDataSource.update(it)
            }
        }
        return null
    }

    suspend fun switchFavorite(pokemon: Pokemon): Error? {
        val updatePokemon = pokemon.copy(favorite = !pokemon.favorite)
        return localDataSource.update(updatePokemon)
    }

    fun getCollectionByPokemon(pokemonID: Int, path: String): Flow<List<GalleryItem>> =
        localDataSource.getCollectionById(pokemonID, path)

    suspend fun saveCollectionByPokemon(id: Int, type: Int, image: String): Error? {
        return localDataSource.saveCollection(id, type, image)
    }
}