package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PhotoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class DeletePhotoUseCaseTest {

    @Mock
    lateinit var photoRepository: PhotoRepository

    @Test
    fun `Invoke calls photo repository`(): Unit = runBlocking {
        val photo = "namePhoto"
        val deletePhotoUseCase = DeletePhotoUseCase(photoRepository)

        deletePhotoUseCase(photo)

        verify(photoRepository).deleteImageFile(photo)
    }
}