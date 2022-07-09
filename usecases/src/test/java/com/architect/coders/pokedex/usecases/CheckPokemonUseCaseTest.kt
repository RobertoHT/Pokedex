package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class CheckPokemonUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val checkPokemonUseCase = CheckPokemonUseCase(pokemonRepository)

        checkPokemonUseCase(1)

        verify(pokemonRepository).checkPokemonDetail(1)
    }
}