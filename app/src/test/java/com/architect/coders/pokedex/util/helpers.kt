package com.architect.coders.pokedex.util

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.framework.database.*
import com.architect.coders.pokedex.framework.database.model.CollectionL
import com.architect.coders.pokedex.framework.database.model.PokemonL
import com.architect.coders.pokedex.framework.database.model.StatL
import com.architect.coders.pokedex.framework.database.model.TypeL
import com.architect.coders.pokedex.framework.network.*
import com.architect.coders.pokedex.framework.network.model.*

fun buildPokemonRepositoryWith(
    localData: List<PokemonL> = emptyList(),
    localType: List<TypeL> = emptyList(),
    localStat: List<StatL> = emptyList(),
    remoteData: List<PokemonItemR> = emptyList(),
    remoteType: List<TypeR> = emptyList(),
    remoteStat: List<StatR> = emptyList(),
    localCollection: List<CollectionL> = arrayListOf()
): PokemonRepository {
    val localDataSource = PokemonRoomDataSource(FakePokemonDao(localData, localType, localStat, localCollection))
    val remoteDataSource = PokemonServerDataSource(FakePokemonRetrofit(remoteData, remoteType, remoteStat))
    val photoDataSource = FakePokemonPhotoDataSource()
    return PokemonRepository(localDataSource, remoteDataSource, photoDataSource)
}

fun buildDatabasePokemon(empty: Boolean, vararg id: Int) = id.map {
    PokemonL(
        it,
        "Pokemon $it",
        if (empty) 0 else 5,
        if (empty) 0 else 10,
        false
    )
}

fun buildDatabaseType(id: Int, vararg type: Int) = type.map {
    TypeL(
        1,
        id,
        "Type $it"
    )
}

fun buildDatabaseStat(id: Int, vararg stat: Int) = stat.map {
    StatL(
        1,
        id,
        "Stat $it",
        10
    )
}

fun buildDatabseCollection(id: Int, vararg type: Int) = type.map {
    CollectionL(
        1,
        id,
        it,
        "photo/IMG_$it"
    )
}

fun buildRemotePokemonItem(vararg id: Int) = id.map {
    PokemonItemR(
        "Pokemon $it",
        "URL_to_pokemon/$it/"
    )
}

fun buildRemoteType(vararg type: Int) = type.map {
    TypeR(
        1,
        TypeX("Type $it", "URL to type")
    )
}

fun buildRemoteStat(vararg stat: Int) = stat.map {
    StatR(
        10,
        1,
        StatX("Stat $it", "URL to stat")
    )
}