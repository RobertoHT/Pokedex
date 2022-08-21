package com.architect.coders.pokedex.framework

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.framework.network.model.PokemonItemR
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.io.IOException

fun PokemonItemR.id() : Int {
    val split = url.split("/")
    return split[split.size - 2].toInt()
}

fun Throwable.toError(): Error = when (this) {
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server
    is FileNotFoundException -> Error.File
    else -> Error.Unknown
}

suspend fun <T> tryCall(action: suspend () -> T): Either<Error, T> = try {
    action().right()
} catch (e: Exception) {
    e.toError().left()
}