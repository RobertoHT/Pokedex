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
class FindPokemonUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val pokemon = flowOf(samplePokemon.copy(1))
        whenever(pokemonRepository.getPokemonDetail(1)).thenReturn(pokemon)
        val findPokemonUseCase = FindPokemonUseCase(pokemonRepository)

        val result = findPokemonUseCase(1)

        assertEquals(pokemon, result)
    }
}