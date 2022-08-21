package com.architect.coders.pokedex.usecases

import arrow.core.right
import com.architect.coders.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CreatePhotoUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls photo create`(): Unit = runBlocking {
        val photo = "namePhoto"
        val image = "pathImage"
        whenever(pokemonRepository.createPhoto(photo)).thenReturn(image.right())
        val createPhotoUseCase = CreatePhotoUseCase(pokemonRepository)

        val result = createPhotoUseCase(photo)

        assertEquals(image.right(), result)
    }
}