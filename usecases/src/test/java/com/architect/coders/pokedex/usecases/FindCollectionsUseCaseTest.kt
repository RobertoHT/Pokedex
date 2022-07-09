package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class FindCollectionsUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val galleryItem = flowOf(listOf(sampleGallery))
        whenever(pokemonRepository.getCollectionByPokemon(1, "path")).thenReturn(galleryItem)
        val findCollectionsUseCase = FindCollectionsUseCase(pokemonRepository)

        val result = findCollectionsUseCase(1, "path")

        assertEquals(galleryItem, result)
    }
}