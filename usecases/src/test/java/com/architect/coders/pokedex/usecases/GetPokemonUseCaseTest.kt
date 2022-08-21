package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.testshared.samplePokemon
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetPokemonUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val pokemon = flowOf(listOf(samplePokemon.copy(1)))
        whenever(pokemonRepository.pokemonList).thenReturn(pokemon)
        val getPokemonUseCase = GetPokemonUseCase(pokemonRepository)

        val result = getPokemonUseCase()

        assertEquals(pokemon, result)
    }
}