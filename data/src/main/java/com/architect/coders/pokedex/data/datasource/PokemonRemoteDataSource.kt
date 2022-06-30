package com.architect.coders.pokedex.data.datasource

import arrow.core.Either
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.Pokemon

interface PokemonRemoteDataSource {

    suspend fun getPokemonList(offset: Int): Either<Error, List<Pokemon>>

    suspend fun getPokemonDetail(pokemonID: Int): Either<Error, Pokemon>
}