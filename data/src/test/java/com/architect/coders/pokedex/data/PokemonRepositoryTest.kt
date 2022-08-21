package com.architect.coders.pokedex.data

import arrow.core.right
import com.architect.coders.pokedex.data.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.data.datasource.PokemonPhotoDataSource
import com.architect.coders.pokedex.data.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.data.repository.PokemonRepository
import com.architect.coders.pokedex.domain.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class PokemonRepositoryTest {

    @Mock
    lateinit var localDataSource: PokemonLocalDataSource

    @Mock
    lateinit var remoteDataSource: PokemonRemoteDataSource

    @Mock
    lateinit var photoDataSource: PokemonPhotoDataSource

    private lateinit var pokemonRepository: PokemonRepository

    private val localPokemon = flowOf(listOf(samplePokemon.copy(1)))

    @Before
    fun setup() {
        whenever(localDataSource.pokemonList).thenReturn(localPokemon)
        pokemonRepository = PokemonRepository(localDataSource, remoteDataSource, photoDataSource)
    }

    @Test
    fun `Pokemon are taken from local if available`(): Unit = runBlocking {
        val result = pokemonRepository.pokemonList

        assertEquals(localPokemon, result)
    }

    @Test
    fun `Pokemon are saved to local when its empty`(): Unit = runBlocking {
        val remotePokemon = listOf(samplePokemon.copy(2))
        whenever(localDataSource.size()).thenReturn(0)
        whenever(remoteDataSource.getPokemonList(any())).thenReturn(remotePokemon.right())

        pokemonRepository.checkRequierePokemonData(0)

        verify(localDataSource).save(remotePokemon)
    }

    @Test
    fun `Pokemon are not called to remote when lastVisible is not close to size local`(): Unit = runBlocking {
        whenever(localDataSource.size()).thenReturn(25)

        pokemonRepository.checkRequierePokemonData(5)

        verify(remoteDataSource, never()).getPokemonList(any())
    }

    @Test
    fun `Finding a pokemon by its id in local`(): Unit = runBlocking {
        val pokemon = flowOf(samplePokemon.copy(3))
        whenever(localDataSource.findById(3)).thenReturn(pokemon)

        val result = pokemonRepository.getPokemonDetail(3)

        assertEquals(pokemon, result)
    }

    @Test
    fun `Pokemon detail are saved to local when its data empty`(): Unit = runBlocking {
        val remotePokemon = samplePokemon.copy(
            id = 4,
            types = listOf(sampleType),
            stats = listOf(sampleStat)
        )
        whenever(localDataSource.isEmpty(any())).thenReturn(true)
        whenever(remoteDataSource.getPokemonDetail(any())).thenReturn(remotePokemon.right())

        pokemonRepository.checkPokemonDetail(any())

        verify(localDataSource).saveTypes(remotePokemon.id, remotePokemon.types)
        verify(localDataSource).saveStats(remotePokemon.id, remotePokemon.stats)
        verify(localDataSource).update(remotePokemon)
    }

    @Test
    fun `Pokemon detail are not called to remote when its data not empty`(): Unit = runBlocking {
        whenever(localDataSource.isEmpty(any())).thenReturn(false)

        pokemonRepository.checkPokemonDetail(any())

        verify(remoteDataSource, never()).getPokemonDetail(any())
    }

    @Test
    fun `Switching favorite marks as favorite an unfavorite pokemon`(): Unit = runBlocking {
        val pokemon = samplePokemon.copy(favorite = false)

        pokemonRepository.switchFavorite(pokemon)

        verify(localDataSource).update(argThat { favorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite pokemon`(): Unit = runBlocking {
        val pokemon = samplePokemon.copy(favorite = true)

        pokemonRepository.switchFavorite(pokemon)

        verify(localDataSource).update(argThat { !favorite })
    }

    @Test
    fun `Finding their photos by pokemon in local`(): Unit = runBlocking {
        val galleryItem = flowOf(listOf(sampleGallery))
        whenever(localDataSource.getCollectionById(1, "path")).thenReturn(galleryItem)

        val result = pokemonRepository.getCollectionByPokemon(1, "path")

        assertEquals(galleryItem, result)
    }

    @Test
    fun `Gallery item are saved to local`(): Unit = runBlocking {
        val id = 1
        val type = 1
        val image = "photo"
        pokemonRepository.saveCollectionByPokemon(id, type, image)

        verify(localDataSource).saveCollection(id, type, image)
    }

    @Test
    fun `Get path for show photo`(): Unit = runBlocking {
        val path = "path"
        whenever(photoDataSource.path).thenReturn(path)

        val result = pokemonRepository.getPhotoPath()

        assertEquals(path, result)
    }

    @Test
    fun `Create a photo file`(): Unit = runBlocking {
        val name = "photoName"
        pokemonRepository.createPhoto(name)

        verify(photoDataSource).createFile(name)
    }

    @Test
    fun `Delete a photo file`(): Unit = runBlocking {
        val name = "photoName"
        pokemonRepository.deletePhoto(name)

        verify(photoDataSource).deleteImageFile(name)
    }
}

private val samplePokemon = Pokemon(
    0,
    "Name",
    0,
    0,
    false,
    emptyList(),
    emptyList()
)

private val sampleType = Type("Name")

private val sampleStat = Stat("Name", 0)

private val sampleGallery = GalleryItem(PokeCollec.AMIIBO, mutableListOf("Photo"))