package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetPathUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls photo path`(): Unit = runBlocking {
        val path = "pathPhoto"
        whenever(pokemonRepository.getPhotoPath()).thenReturn(path)
        val getPathUseCase = GetPathUseCase(pokemonRepository)

        val result = getPathUseCase()

        assertEquals(path, result)
    }
}