package com.architect.coders.pokedex.util

import arrow.core.Either
import arrow.core.right
import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.framework.database.*
import com.architect.coders.pokedex.framework.network.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePokemonDao(
    pokemon: List<PokemonL> = emptyList(),
    types: List<TypeL> = emptyList(),
    stats: List<StatL> = emptyList(),
    collections: List<CollectionL> = arrayListOf()
) : PokemonDAO {

    private val inMemoryPokemon = MutableStateFlow(pokemon)
    private lateinit var findPokemonFlow: MutableStateFlow<PokemonDetailL>
    private var pokemonTypes = types
    private var pokemonStats = stats
    private val inMemoryCollections = MutableStateFlow(collections)

    override fun getAllPokemon(): Flow<List<PokemonL>> = inMemoryPokemon

    override suspend fun pokemonCount(): Int = inMemoryPokemon.value.size

    override suspend fun insertPokemon(pokemonList: List<PokemonL>) {
        inMemoryPokemon.value = pokemonList
    }

    override suspend fun isPokemonByIDEmpty(id: Int): Int {
        inMemoryPokemon.value.firstOrNull {
            it.id == id
        }?.let {
            return if (it.height == 0 && it.weight == 0) 1 else 0
        }

        return 0
    }

    override suspend fun updatePokemon(pokemonL: PokemonL) {
        if (::findPokemonFlow.isInitialized) {
            findPokemonFlow.value = PokemonDetailL(pokemonL, pokemonTypes, pokemonStats)
        } else {
            findPokemonFlow = MutableStateFlow(PokemonDetailL(pokemonL, pokemonTypes, pokemonStats))
        }
    }

    override suspend fun insertTypes(types: List<TypeL>) {
        pokemonTypes = types
    }

    override suspend fun insertStats(stats: List<StatL>) {
        pokemonStats = stats
    }

    override fun findPokemonByID(id: Int): Flow<PokemonDetailL> {
        if (!::findPokemonFlow.isInitialized) {
            findPokemonFlow = MutableStateFlow(inMemoryPokemon.value.first { it.id == id }
                .let { PokemonDetailL(it, pokemonTypes, pokemonStats) })
        }
        return findPokemonFlow
    }

    override fun getAllCollectionByPokemon(id: Int): Flow<List<CollectionL>> = inMemoryCollections

    override suspend fun insertCollection(collection: CollectionL) {
        inMemoryCollections.value = listOf(collection)
    }

}


class FakePokemonRetrofit(
    private val pokemon: List<PokemonItemR> = emptyList(),
    private val types: List<TypeR> = emptyList(),
    private val stats: List<StatR> = emptyList()
) : PokeService {

    override suspend fun getPokemonList(offset: Int) = PokemonListResult(
        0,
        "next",
        "previous",
        pokemon
    )

    override suspend fun getPokemonDetail(id: Int) = PokemonDetailR(
        5,
        10,
        id,
        false,
        "Location Area",
        "Pokemon $id",
        1,
        Species("Name Specie", "URL to specie"),
        stats,
        types,
        5
    )

}


class FakePhotoRepository : PhotoRepository {

    private lateinit var file: String

    override val path = "path/"

    override fun createFile(nameFile: String): Either<Error, String> {
        file = "$path$nameFile"
        return file.right()
    }

    override fun deleteImageFile(fileName: String): Error? {
        file = ""
        return null
    }

}