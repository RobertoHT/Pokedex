package com.architect.coders.pokedex.ui.main

import app.cash.turbine.test
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.testshared.samplePokemon
import com.architect.coders.pokedex.ui.main.MainViewModel.*
import com.architect.coders.pokedex.usecases.GetPokemonUseCase
import com.architect.coders.pokedex.usecases.RequestPokemonUseCase
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
class MainViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getPokemonUseCase: GetPokemonUseCase

    @Mock
    lateinit var requestPokemonUseCase: RequestPokemonUseCase

    private lateinit var vm: MainViewModel

    private val pokemon = listOf(samplePokemon.copy(1))

    @Test
    fun `State is updated with current cached content`() = runTest {
        whenever(getPokemonUseCase()).thenReturn(flowOf(pokemon))
        vm = MainViewModel(getPokemonUseCase, requestPokemonUseCase)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(pokemonList = pokemon), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting`() = runTest {
        whenever(getPokemonUseCase()).thenReturn(flowOf(emptyList()))
        vm = MainViewModel(getPokemonUseCase, requestPokemonUseCase)

        vm.validateData()

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())
            assertEquals(UIState(loading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Pokemon are requested when UI screen starts and there is no data`() = runTest {
        whenever(getPokemonUseCase()).thenReturn(flowOf(emptyList()))
        vm = MainViewModel(getPokemonUseCase, requestPokemonUseCase)

        vm.validateData()
        runCurrent()

        verify(requestPokemonUseCase).invoke(0)
    }
}