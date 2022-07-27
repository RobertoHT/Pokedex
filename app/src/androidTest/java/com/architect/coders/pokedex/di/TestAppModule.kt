package com.architect.coders.pokedex.di

import com.architect.coders.pokedex.framework.database.PokemonDAO
import com.architect.coders.pokedex.framework.network.PokeService
import com.architect.coders.pokedex.util.FakePokemonDao
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
    fun provideDao(): PokemonDAO = FakePokemonDao()

    @Provides
    @Singleton
    fun provideRemoteClient(): PokeService = FakePokemonRetrofit(buildRemotePokemonItem(1, 2, 3, 4, 5))
}