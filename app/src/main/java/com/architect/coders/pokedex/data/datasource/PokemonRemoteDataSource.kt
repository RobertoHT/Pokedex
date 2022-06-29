package com.architect.coders.pokedex.data.datasource

import com.architect.coders.pokedex.data.network.*
import com.architect.coders.pokedex.domain.Pokemon
import com.architect.coders.pokedex.domain.Stat
import com.architect.coders.pokedex.domain.Type
import com.architect.coders.pokedex.ui.common.id

class PokemonRemoteDataSource {

    suspend fun getPokemonList(offset: Int): List<Pokemon> =
        PokeClient.service.getPokemonList(offset).pokemonItems.toDomainModel()

    suspend fun getPokemonDetail(pokemonID: Int): Pokemon =
        PokeClient.service.getPokemonDetail(pokemonID).toDomainModel()

    private fun List<PokemonItemR>.toDomainModel(): List<Pokemon> =
        map { it.toDomainModel() }

    private fun PokemonItemR.toDomainModel(): Pokemon = Pokemon(
        id(),
        name
    )

    private fun PokemonDetailR.toDomainModel(): Pokemon = Pokemon(
        id, name, weight, height, false, types.toDomainTypeModel(), stats.toDomainStatModel()
    )

    private fun List<TypeR>.toDomainTypeModel(): List<Type> =
        map { it.toDomainModel() }

    private fun TypeR.toDomainModel(): Type = Type(
        type.name
    )

    private fun List<StatR>.toDomainStatModel(): List<Stat> =
        map { it.toDomainModel() }

    private fun StatR.toDomainModel(): Stat = Stat(
        stat.name, baseStat
    )
}