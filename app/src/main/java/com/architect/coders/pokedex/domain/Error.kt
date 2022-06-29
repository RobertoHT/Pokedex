package com.architect.coders.pokedex.domain

import retrofit2.HttpException
import java.io.FileNotFoundException
import java.io.IOException

sealed interface Error {
    object Server : Error
    object Connectivity : Error
    object File : Error
    object Unknown : Error
}

fun Throwable.toError(): Error = when (this) {
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server
    is FileNotFoundException -> Error.File
    else -> Error.Unknown
}

inline fun <T> tryCall(action: () -> T): Error? = try {
    action()
    null
} catch (e: Exception) {
    e.toError()
}