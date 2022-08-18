package com.architect.coders.pokedex.ui.main

import app.cash.turbine.test
import com.architect.coders.pokedex.util.buildDatabasePokemon
import com.architect.coders.pokedex.util.buildPokemonRepositoryWith
import com.architect.coders.pokedex.util.buildRemotePokemonItem
import com.architect.coders.pokedex.framework.database.model.PokemonL
import com.architect.coders.pokedex.framework.network.model.PokemonItemR
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.ui.main.MainViewModel.*
import com.architect.coders.pokedex.usecases.GetPokemonUseCase
import com.architect.coders.pokedex.usecases.RequestPokemonUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from server when local source is empty`() = runTest {
        val remoteData = buildRemotePokemonItem(1, 3, 5)
        val vm = buildViewModelWith(remoteData = remoteData)

        vm.validateData()

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())
            assertEquals(UIState(loading = false), awaitItem())

            val pokemon = awaitItem().pokemonList!!
            assertEquals("Pokemon 1", pokemon[0].name)
            assertEquals("Pokemon 3", pokemon[1].name)
            assertEquals("Pokemon 5", pokemon[2].name)

            cancel()
        }
    }

    @Test
    fun `Data is loaded from local source when is available`() = runTest {
        val localData = buildDatabasePokemon(true, 2, 4, 6)
        val vm = buildViewModelWith(localData = localData)

        vm.state.test {
            assertEquals(UIState(), awaitItem())

            val pokemon = awaitItem().pokemonList!!
            assertEquals("Pokemon 2", pokemon[0].name)
            assertEquals("Pokemon 4", pokemon[1].name)
            assertEquals("Pokemon 6", pokemon[2].name)

            cancel()
        }
    }

    private fun buildViewModelWith(
        localData: List<PokemonL> = emptyList(),
        remoteData: List<PokemonItemR> = emptyList()
    ): MainViewModel {
        val pokemonRepository = buildPokemonRepositoryWith(localData = localData, remoteData = remoteData)
        val getPokemonUseCase = GetPokemonUseCase(pokemonRepository)
        val requestPokemonUseCase = RequestPokemonUseCase(pokemonRepository)
        return MainViewModel(getPokemonUseCase, requestPokemonUseCase)
    }
}