package com.architect.coders.pokedex.ui.detail

import app.cash.turbine.test
import com.architect.coders.pokedex.testrules.CoroutinesTestRule
import com.architect.coders.pokedex.testshared.samplePokemon
import com.architect.coders.pokedex.ui.detail.DetailViewModel.*
import com.architect.coders.pokedex.usecases.CheckPokemonUseCase
import com.architect.coders.pokedex.usecases.FindPokemonUseCase
import com.architect.coders.pokedex.usecases.SwitchPokemonFavoriteUseCase
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
class DetailViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var checkPokemonUseCase: CheckPokemonUseCase

    @Mock
    lateinit var findPokemonUseCase: FindPokemonUseCase

    @Mock
    lateinit var switchPokemonFavoriteUseCase: SwitchPokemonFavoriteUseCase

    private lateinit var vm: DetailViewModel

    private val pokemonID = 1
    private val pokemonColor = 2
    private val pokemon = samplePokemon.copy(pokemonID)

    @Before
    fun setup() {
        whenever(findPokemonUseCase(pokemonID)).thenReturn(flowOf(pokemon))
        vm = DetailViewModel(
            pokemonID,
            pokemonColor,
            checkPokemonUseCase,
            findPokemonUseCase,
            switchPokemonFavoriteUseCase
        )
    }

    @Test
    fun `UI is updated with the pokemon on start`() = runTest {
        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())
            assertEquals(UIState(pokemon = pokemon, colorSwatch = pokemonColor, views = true), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Pokemon detail are requested when UI screen starts and theres no data`() = runTest {
        runCurrent()

        verify(checkPokemonUseCase).invoke(pokemonID)
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting`() = runTest {
        vm.state.test {
            assertEquals(UIState(), awaitItem())
            assertEquals(UIState(loading = true), awaitItem())
            assertEquals(UIState(pokemon = pokemon, colorSwatch = pokemonColor, views = true, loading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite action calls the corresponding use case`() = runTest {
        vm.onFavoriteClicked()
        runCurrent()

        verify(switchPokemonFavoriteUseCase).invoke(pokemon)
    }
}