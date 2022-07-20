package com.architect.coders.pokedex.di

import android.app.Application
import androidx.room.Room
import com.architect.coders.pokedex.data.PhotoRepository
import com.architect.coders.pokedex.data.datasource.PokemonLocalDataSource
import com.architect.coders.pokedex.data.datasource.PokemonRemoteDataSource
import com.architect.coders.pokedex.framework.FileRepository
import com.architect.coders.pokedex.framework.database.PokemonDatabase
import com.architect.coders.pokedex.framework.database.PokemonRoomDataSource
import com.architect.coders.pokedex.framework.network.PokeService
import com.architect.coders.pokedex.framework.network.PokemonServerDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(
            app,
            PokemonDatabase::class.java,
            "pokemon-db"
        ).build()

    @Provides
    @Singleton
    fun provideDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    @Singleton
    fun provideRemoteClient(): PokeService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PokeService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindPhotoRepository(photoRepository: FileRepository): PhotoRepository

    @Binds
    abstract fun bindLocalDataSource(localDataSource: PokemonRoomDataSource): PokemonLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: PokemonServerDataSource): PokemonRemoteDataSource
}