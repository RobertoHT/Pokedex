package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.data.database.*
import com.architect.coders.pokedex.domain.GalleryItem
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.domain.Stat
import com.architect.coders.pokedex.domain.Type
import com.architect.coders.pokedex.ui.common.getTypeById
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonLocalDataSource(private val pokemonDao: PokemonDAO) {

    val pokemonList: Flow<List<Pokemon>> = pokemonDao.getAllPokemon().map { it.toDomainModel() }

    suspend fun size(): Int = pokemonDao.pokemonCount()

    suspend fun save(pokemonList: List<Pokemon>) {
        pokemonDao.insertPokemon(pokemonList.fromDomainModel())
    }

    suspend fun isEmpty(id: Int): Boolean = pokemonDao.isPokemonByIDEmpty(id) == 1

    fun findById(id: Int): Flow<Pokemon> = pokemonDao.findPokemonByID(id).map { it.toDomainModel() }

    suspend fun update(pokemon: Pokemon) = pokemonDao.updatePokemon(pokemon.fromDomainModel())

    suspend fun saveTypes(id: Int, types: List<Type>) = pokemonDao.insertTypes(types.fromDomainTypeModel(id))

    suspend fun saveStats(id: Int, stats: List<Stat>) = pokemonDao.insertStats(stats.fromDomainStatModel(id))

    fun getCollectionById(id: Int, path: String): Flow<List<GalleryItem>> =
        pokemonDao.getAllCollectionByPokemon(id).map { it.toDomainModel(path) }

    suspend fun saveCollection(id: Int, type: Int, image: String) = pokemonDao.insertCollection(
        CollectionL(
        0, id, type, image
    )
    )

    private fun List<PokemonL>.toDomainModel(): List<Pokemon> = map { it.toDomainModel() }

    private fun PokemonL.toDomainModel(): Pokemon = Pokemon(
        id, name, weight, height, favorite
    )

    private fun PokemonDetailL.toDomainModel(): Pokemon = Pokemon(
        pokemon.id, pokemon.name, pokemon.weight, pokemon.height, pokemon.favorite,
        types.toDomainTypeModel(), stats.toDomainStatModel()
    )

    private fun List<TypeL>.toDomainTypeModel(): List<Type> = map { it.toDomainModel() }

    private fun TypeL.toDomainModel(): Type = Type(
        name
    )

    private fun List<StatL>.toDomainStatModel(): List<Stat> = map { it.toDomainModel() }

    private fun StatL.toDomainModel(): Stat = Stat(
        name, baseStat
    )

    private fun List<Pokemon>.fromDomainModel(): List<PokemonL> = map { it.fromDomainModel() }

    private fun Pokemon.fromDomainModel(): PokemonL = PokemonL(
        id, name, weight, height, favorite
    )

    private fun List<Type>.fromDomainTypeModel(id: Int): List<TypeL> = map { it.fromDomainModel(id) }

    private fun Type.fromDomainModel(id: Int): TypeL = TypeL(
        pokemonID = id,
        name = name
    )

    private fun List<Stat>.fromDomainStatModel(id: Int): List<StatL> = map { it.fromDomainModel(id) }

    private fun Stat.fromDomainModel(id: Int): StatL = StatL(
        pokemonID = id,
        name = name,
        baseStat = baseStat
    )

    private fun List<CollectionL>.toDomainModel(path: String): List<GalleryItem> =
        groupBy { it.type }
            .toList()
            .map { GalleryItem(it.first.getTypeById(), it.second.toStringList(path)) }

    private fun List<CollectionL>.toStringList(path: String): MutableList<String> =
        map { "$path/${it.photo}" }.toMutableList()
}