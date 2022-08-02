package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PhotoRepository
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
    lateinit var photoRepository: PhotoRepository

    @Test
    fun `Invoke calls photo repository`(): Unit = runBlocking {
        val path = "pathPhoto"
        whenever(photoRepository.path).thenReturn(path)
        val getPathUseCase = GetPathUseCase(photoRepository)

        val result = getPathUseCase()

        assertEquals(path, result)
    }
}