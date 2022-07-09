package com.architect.coders.pokedex.usecases

import com.architect.coders.pokedex.data.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class SwitchPokemonFavoriteUseCaseTest {

    @Mock
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `Invoke calls pokemon repository`(): Unit = runBlocking {
        val pokemon = samplePokemon.copy(1)
        val switchPokemonFavoriteUseCase = SwitchPokemonFavoriteUseCase(pokemonRepository)

        switchPokemonFavoriteUseCase(pokemon)

        verify(pokemonRepository).switchFavorite(pokemon)
    }
}