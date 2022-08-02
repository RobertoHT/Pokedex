package com.architect.coders.pokedex.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.architect.coders.pokedex.R
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

    @Test
    fun click_a_pokemon_navigates_to_detail() {
        onView(withId(R.id.recycler))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.nameDetail))
            .check(matches(withText("bulbasaur")))
        onView(withId(R.id.weightDetail))
            .check(matches(withText("69 kg")))
        onView(withId(R.id.heightDetail))
            .check(matches(withText("7 m")))
    }
}