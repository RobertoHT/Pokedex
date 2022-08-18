package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class RequestPokemonUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val requestPokemonUseCase = RequestPokemonUseCase(pokemonRepository)

        requestPokemonUseCase(1)

        verify(pokemonRepository).checkRequierePokemonData(1)
    }
}