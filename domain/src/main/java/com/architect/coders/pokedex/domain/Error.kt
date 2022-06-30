package com.architect.coders.pokedex.domain

sealed interface Error {
    object Server : Error
    object Connectivity : Error
    object File : Error
    object Unknown : Error
}