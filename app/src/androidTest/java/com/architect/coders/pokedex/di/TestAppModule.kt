package com.architect.coders.pokedex.di

import android.app.Application
import androidx.room.Room
import com.architect.coders.pokedex.framework.database.PokemonDatabase
import com.architect.coders.pokedex.framework.network.PokeService
import com.architect.coders.pokedex.util.FakePokemonRetrofit
import com.architect.coders.pokedex.util.buildRemotePokemonItem
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.inMemoryDatabaseBuilder(
            app,
            PokemonDatabase::class.java
        ).build()

    @Provides
    @Singleton
    fun provideDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    @Singleton
    fun provideRemoteClient(): PokeService = FakePokemonRetrofit(buildRemotePokemonItem(1, 2, 3, 4, 5))
}