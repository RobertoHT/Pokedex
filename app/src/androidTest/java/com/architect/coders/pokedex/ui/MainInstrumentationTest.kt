package com.architect.coders.pokedex.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.architect.coders.pokedex.data.MockWebServerRule
import com.architect.coders.pokedex.data.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.framework.database.PokemonDAO
import com.architect.coders.pokedex.util.buildDatabasePokemon
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var pokemonDao: PokemonDAO

    @Inject
    lateinit var remoteDataSource: PokemonRemoteDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest {
        pokemonDao.insertPokemon(buildDatabasePokemon(true, 1, 2, 3, 4))
        assertEquals(4, pokemonDao.pokemonCount())
    }

    @Test
    fun check_8_items_db() = runTest {
        pokemonDao.insertPokemon(buildDatabasePokemon(true, 5, 6, 7, 8, 9, 10, 11, 12))
        assertEquals(8, pokemonDao.pokemonCount())
    }

    @Test
    fun check_pokemon_detail_is_empty_db() = runTest {
        pokemonDao.insertPokemon(buildDatabasePokemon(true, 1, 2, 3))
        assertEquals(1, pokemonDao.isPokemonByIDEmpty(2))
    }

    @Test
    fun check_pokemon_detail_is_not_empty_db() = runTest {
        pokemonDao.insertPokemon(buildDatabasePokemon(false, 1, 2, 3))
        assertEquals(0, pokemonDao.isPokemonByIDEmpty(3))
    }

    @Test
    fun check_pokemon_list_remote_is_working() = runTest {
        val pokemon = remoteDataSource.getPokemonList(0)
        pokemon.fold({ throw Exception(it.toString()) }) {
            assertEquals("bulbasaur", it[0].name)
        }
    }

    @Test
    fun check_pokemon_detail_remote_is_working() = runTest {
        val pokemon = remoteDataSource.getPokemonDetail(1)
        pokemon.fold({ throw Exception(it.toString()) }) {
            assertEquals("bulbasaur", it.name)
            assertEquals(7, it.height)
            assertEquals(69, it.weight)
        }
    }
}