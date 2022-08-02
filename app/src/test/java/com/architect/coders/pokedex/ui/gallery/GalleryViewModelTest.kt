package com.architect.coders.pokedex.ui.gallery

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.architect.coders.pokedex.domain.Error
import com.architect.coders.pokedex.domain.PokeCollec
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.testshared.sampleGallery
import com.architect.coders.pokedex.ui.gallery.GalleryViewModel.*
import com.architect.coders.pokedex.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GalleryViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var findCollectionsUseCase: FindCollectionsUseCase

    @Mock
    lateinit var saveCollectionUseCase: SaveCollectionUseCase

    @Mock
    lateinit var getPathUseCase: GetPathUseCase

    @Mock
    lateinit var createPhotoUseCase: CreatePhotoUseCase

    @Mock
    lateinit var deletePhotoUseCase: DeletePhotoUseCase

    private lateinit var vm: GalleryViewModel

    private val pokemonID = 1
    private val photoName = "Poke_1_3_"
    private val photoPath = "path/$photoName"
    private val collections = listOf(sampleGallery)

    @Before
    fun setup() {
        whenever(findCollectionsUseCase(pokemonID, getPathUseCase())).thenReturn(flowOf(collections))
        vm = GalleryViewModel(
            pokemonID,
            findCollectionsUseCase,
            saveCollectionUseCase,
            getPathUseCase,
            createPhotoUseCase,
            deletePhotoUseCase
        )
    }

    @Test
    fun `UI is updated with the collection on start`() = runTest {
        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = collections), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Find collections action calls the corresponding use case`() = runTest {
        verify(getPathUseCase).invoke()
    }

    @Test
    fun `Other action calls the corresponding use case`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn("path".right())

        vm.onCreatePictureFile(1)
        runCurrent()

        verify(createPhotoUseCase).invoke(photoName)
    }

    @Test
    fun `Path image and name image are get on create picture use case`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn(photoPath.right())

        vm.onCreatePictureFile(1)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = collections), awaitItem())
            assertEquals(UIState(
                colletionList = collections,
                nameImage = photoName,
                pathImage = photoPath,
                type = PokeCollec.OTHER
            ), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Catch error when create picture use case return a exception`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn(Error.File.left())

        vm.onCreatePictureFile(1)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = collections), awaitItem())
            assertEquals(UIState(colletionList = collections, error = Error.File), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Camera agree return calls the corresponding use`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn(photoPath.right())

        vm.onCreatePictureFile(1)
        vm.onPictureReady(true)
        runCurrent()

        verify(saveCollectionUseCase).invoke(pokemonID, PokeCollec.OTHER.id, photoName)
    }

    @Test
    fun `Camera disagree return calls the corresponding use`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn(photoPath.right())

        vm.onCreatePictureFile(1)
        vm.onPictureReady(false)
        runCurrent()

        verify(deletePhotoUseCase).invoke(photoName)
    }

    @Test
    fun `Finished captured image from camera`() = runTest {
        whenever(createPhotoUseCase(photoName)).thenReturn(photoPath.right())

        vm.onCreatePictureFile(1)
        vm.onPictureReady(true)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = collections), awaitItem())
            assertEquals(UIState(
                colletionList = collections,
                nameImage = photoName,
                pathImage = photoPath,
                type = PokeCollec.OTHER
            ), awaitItem())
            assertEquals(UIState(
                colletionList = collections,
                pathImage = photoPath,
            ), awaitItem())
            cancel()
        }
    }
}