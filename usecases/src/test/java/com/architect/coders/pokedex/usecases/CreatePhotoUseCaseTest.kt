package com.architect.coders.pokedex.usecases

import arrow.core.right
import com.architect.coders.pokedex.data.PhotoRepository
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
    lateinit var photoRepository: PhotoRepository

    @Test
    fun `Invoke calls photo repository`(): Unit = runBlocking {
        val photo = "namePhoto"
        val image = "pathImage"
        whenever(photoRepository.createFile(photo)).thenReturn(image.right())
        val createPhotoUseCase = CreatePhotoUseCase(photoRepository)

        val result = createPhotoUseCase(photo)

        assertEquals(image.right(), result)
    }
}