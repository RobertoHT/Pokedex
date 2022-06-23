package com.architect.coders.pokedex.data

import com.architect.coders.pokedex.App
import com.architect.coders.pokedex.common.id
import com.architect.coders.pokedex.database.*
import com.architect.coders.pokedex.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.network.PokemonDetailR
import com.architect.coders.pokedex.network.PokemonItemR
import com.architect.coders.pokedex.network.StatR
import com.architect.coders.pokedex.network.TypeR
import kotlinx.coroutines.flow.Flow

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

    fun getPokemonDetail(pokemonID: Int): Flow<PokemonDetailL> = localDataSource.findById(pokemonID)

    suspend fun checkPokemonDetail(pokemonID: Int) {
        if (localDataSource.isEmpty(pokemonID)) {
            val pokemonDetail = remoteDataSource.getPokemonDetail(pokemonID)
            savePokemonDetail(pokemonDetail)
        }
    }

    private suspend fun savePokemonDetail(pokemonDetail: PokemonDetailR) {
        val id = pokemonDetail.id
        localDataSource.saveTypes(pokemonDetail.types.toTypeLocalModel(id))
        localDataSource.saveStats(pokemonDetail.stats.toStatLocalModel(id))
        localDataSource.update(pokemonDetail.toLocalModel())
    }

    suspend fun switchFavorite(pokemon: PokemonL) {
        val updatePokemon = pokemon.copy(favorite = !pokemon.favorite)
        localDataSource.update(updatePokemon)
    }

    fun getCollectionByPokemon(pokemonID: Int): Flow<List<CollectionL>> = localDataSource.getCollectionById(pokemonID)

    suspend fun saveCollectionByPokemon(collection: CollectionL) {
        localDataSource.saveCollection(collection)
    }

    private fun List<PokemonItemR>.toLocalModel(): List<PokemonL> = map { it.toLocalModel() }

    private fun PokemonItemR.toLocalModel(): PokemonL = PokemonL(id(), name, 0, 0, false)

    private fun PokemonDetailR.toLocalModel(): PokemonL = PokemonL(id, name, weight, height, false)

    private fun List<TypeR>.toTypeLocalModel(id: Int): List<TypeL> = map { it.toLocalModel(id) }

    private fun TypeR.toLocalModel(id: Int): TypeL = TypeL(
        pokemonID = id,
        name = type.name
    )

    private fun List<StatR>.toStatLocalModel(id: Int): List<StatL> = map { it.toLocalModel(id) }

    private fun StatR.toLocalModel(id: Int): StatL = StatL(
        pokemonID = id,
        name = stat.name,
        baseStat = baseStat
    )
}