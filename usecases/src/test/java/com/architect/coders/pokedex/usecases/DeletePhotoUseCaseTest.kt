package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class DeletePhotoUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls photo delete`(): Unit = runBlocking {
        val photo = "namePhoto"
        val deletePhotoUseCase = DeletePhotoUseCase(pokemonRepository)

        deletePhotoUseCase(photo)

        verify(pokemonRepository).deletePhoto(photo)
    }
}