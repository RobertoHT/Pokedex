package com.architect.coders.pokedex.data

import com.architect.coders.pokedex.App
import com.architect.coders.pokedex.common.id
import com.architect.coders.pokedex.database.PokemonL
import com.architect.coders.pokedex.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.model.PokemonItem
import com.architect.coders.pokedex.network.PokeClient

private const val PAGE_THRESHOLD = 6

class PokemonRepository(application: App) {

    private val localDataSource = PokemonLocalDataSource(application.db.pokemonDao())
    private val remoteDataSource = PokemonRemoteDataSource()

    val pokemonList = localDataSource.pokemonList

    suspend fun checkRequierePokemonData(lastVisible: Int) {
        val size = localDataSource.size()

        if (lastVisible >= size - PAGE_THRESHOLD) {
            val pokemon = remoteDataSource.getPokemonList(size)
            localDataSource.save(pokemon.pokemonItems.toLocalModel())
        }
    }

    suspend fun getPokemonDetail(pokemonID: Int) =
        PokeClient.service.getPokemonDetail(pokemonID)

    private fun List<PokemonItem>.toLocalModel(): List<PokemonL> = map { it.toLocalModel() }

    private fun PokemonItem.toLocalModel(): PokemonL = PokemonL(
        id(),
        name,
        0,
        0
    )
}