package com.architect.coders.pokedex.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var pokemonDao: PokemonDAO

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
}