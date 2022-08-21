package com.architect.coders.pokedex.ui.detail

import app.cash.turbine.test
import com.architect.coders.pokedex.framework.database.model.PokemonL
import com.architect.coders.pokedex.framework.database.model.StatL
import com.architect.coders.pokedex.framework.database.model.TypeL
import com.architect.coders.pokedex.framework.network.model.StatR
import com.architect.coders.pokedex.framework.network.model.TypeR
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.ui.detail.DetailViewModel.*
import com.architect.coders.pokedex.usecases.CheckPokemonUseCase
import com.architect.coders.pokedex.usecases.FindPokemonUseCase
import com.architect.coders.pokedex.usecases.SwitchPokemonFavoriteUseCase
import com.architect.coders.pokedex.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from server when detail is empty`() = runTest {
        val localData = buildDatabasePokemon(true, 1, 3, 5)
        val remoteType = buildRemoteType(1, 2, 3)
        val remoteStat = buildRemoteStat(1, 2, 3)
        val vm = buildViewModelWith(1, 5, localData = localData, remoteType = remoteType, remoteStat = remoteStat)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())

            val state = awaitItem()
            assertEquals("Pokemon 1", state.pokemon!!.name)
            assertEquals(5, state.pokemon!!.weight)
            assertEquals(10, state.pokemon!!.height)
            assertEquals(5, state.colorSwatch)
            assertEquals(true, state.views)
            assertEquals("Type 1", state.pokemon!!.types[0].name)
            assertEquals("Stat 1", state.pokemon!!.stats[0].name)
        }
    }

    @Test
    fun `Data is loaded from local when is available`() = runTest {
        val localData = buildDatabasePokemon(false, 2, 4, 6)
        val localType = buildDatabaseType(2, 3, 2, 1)
        val localStat = buildDatabaseStat(2, 5, 6, 7)
        val vm = buildViewModelWith(2, 8, localData = localData, localType = localType, localStat = localStat)

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())

            val state = awaitItem()
            assertEquals("Pokemon 2", state.pokemon!!.name)
            assertEquals(5, state.pokemon!!.weight)
            assertEquals(10, state.pokemon!!.height)
            assertEquals(8, state.colorSwatch)
            assertEquals(true, state.views)
            assertEquals("Type 3", state.pokemon!!.types[0].name)
            assertEquals("Stat 5", state.pokemon!!.stats[0].name)
        }
    }

    @Test
    fun `Favorite is updated in local`() = runTest {
        val localData = buildDatabasePokemon(false, 3, 5, 7)
        val vm = buildViewModelWith(3, 4, localData = localData)

        vm.onFavoriteClicked()

        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())
            assertEquals(false, awaitItem().pokemon!!.favorite)
            assertEquals(true, awaitItem().pokemon!!.favorite)
        }
    }

    private fun buildViewModelWith(
        id: Int,
        colorSwatch: Int,
        localData: List<PokemonL> = emptyList(),
        localType: List<TypeL> = emptyList(),
        localStat: List<StatL> = emptyList(),
        remoteType: List<TypeR> = emptyList(),
        remoteStat: List<StatR> = emptyList()
    ): DetailViewModel {
        val pokemonRepository = buildPokemonRepositoryWith(
            localData = localData,
            localType = localType,
            localStat = localStat,
            remoteType = remoteType,
            remoteStat = remoteStat
        )
        val checkPokemonUseCase = CheckPokemonUseCase(pokemonRepository)
        val findPokemonUseCase = FindPokemonUseCase(pokemonRepository)
        val switchPokemonFavoriteUseCase = SwitchPokemonFavoriteUseCase(pokemonRepository)
        return DetailViewModel(id, colorSwatch, checkPokemonUseCase, findPokemonUseCase, switchPokemonFavoriteUseCase)
    }
}