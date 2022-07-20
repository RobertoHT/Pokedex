package com.architect.coders.pokedex.ui.collection

import app.cash.turbine.test
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.ui.collection.CollectionViewModel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CollectionViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var vm: CollectionViewModel
    private val photoPath = "photoName"

    @Test
    fun `UI is updated with the photo path on start`() = runTest {
        vm = CollectionViewModel(photoPath)

        vm.state.test {
            assertEquals(UIState(image = photoPath), awaitItem())
            cancel()
        }
    }
}