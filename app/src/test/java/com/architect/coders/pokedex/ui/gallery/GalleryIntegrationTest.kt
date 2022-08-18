package com.architect.coders.pokedex.ui.gallery

import app.cash.turbine.test
import com.architect.coders.pokedex.domain.PokeCollec.*
import com.architect.coders.pokedex.framework.database.model.CollectionL
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.ui.gallery.GalleryViewModel.*
import com.architect.coders.pokedex.usecases.*
import com.architect.coders.pokedex.util.buildDatabseCollection
import com.architect.coders.pokedex.util.buildPhotoRepository
import com.architect.coders.pokedex.util.buildPokemonRepositoryWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from local when available`() = runTest {
        val localCollection = buildDatabseCollection(1, PLUSH.id, PLUSH.id, OTHER.id)
        val vm = buildViewModelWith(1, localCollection)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            val collections = awaitItem().colletionList!!
            assertEquals(2, collections.size)
            assertEquals(2, collections[0].photos.size)
            assertEquals(1, collections[1].photos.size)
        }
    }

    @Test
    fun `Select other option and ready to dispatch camera`() = runTest {
        val name = "Poke_2_${OTHER.id}_"
        val vm = buildViewModelWith(2)
        vm.onCreatePictureFile(0)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = emptyList()), awaitItem())
            assertEquals(UIState(
                colletionList = emptyList(),
                nameImage = name,
                type = OTHER,
                pathImage = "path/$name"
            ), awaitItem())
        }
    }

    @Test
    fun `Cancel photo capture and remove temporary data`() = runTest {
        val name = "Poke_3_${OTHER.id}_"
        val vm = buildViewModelWith(3)
        vm.onCreatePictureFile(0)
        vm.onPictureReady(false)
        vm.onUriDone()

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = emptyList()), awaitItem())
            assertEquals(UIState(
                colletionList = emptyList(),
                nameImage = name,
                type = OTHER,
                pathImage = "path/$name"
            ), awaitItem())
            assertEquals(UIState(
                colletionList = emptyList(),
                pathImage = "path/$name"
            ), awaitItem())
            assertEquals(UIState(colletionList = emptyList()), awaitItem())
        }
    }

    @Test
    fun `Save photo capture and UI is updated`() = runTest {
        val name = "Poke_4_${OTHER.id}_"
        val vm = buildViewModelWith(4)
        vm.onCreatePictureFile(0)
        vm.onPictureReady(true)
        vm.onUriDone()

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(colletionList = emptyList()), awaitItem())
            assertEquals(UIState(
                colletionList = emptyList(),
                nameImage = name,
                type = OTHER,
                pathImage = "path/$name"
            ), awaitItem())
            assertEquals(UIState(
                colletionList = emptyList(),
                pathImage = "path/$name"
            ), awaitItem())
            assertEquals(UIState(colletionList = emptyList()), awaitItem())

            val collections = awaitItem().colletionList!!
            assertEquals(1, collections.size)
            assertEquals(1, collections[0].photos.size)
            assertEquals("path/Poke_4_${OTHER.id}_", collections[0].photos[0])
        }
    }

    private fun buildViewModelWith(
        id: Int,
        localCollection: List<CollectionL> = arrayListOf()
    ): GalleryViewModel {
        val pokemonRepository = buildPokemonRepositoryWith(localCollection = localCollection)
        val photoRepository = buildPhotoRepository()
        val findCollectionsUseCase = FindCollectionsUseCase(pokemonRepository)
        val saveCollectionUseCase = SaveCollectionUseCase(pokemonRepository)
        val getPathUseCase = GetPathUseCase(photoRepository)
        val createPhotoUseCase = CreatePhotoUseCase(photoRepository)
        val deletePhotoUseCase = DeletePhotoUseCase(photoRepository)
        return GalleryViewModel(
            id,
            findCollectionsUseCase,
            saveCollectionUseCase,
            getPathUseCase,
            createPhotoUseCase,
            deletePhotoUseCase
        )
    }
}