package com.architect.coders.pokedex.di

import android.app.Application
import androidx.room.Room
import com.architect.coders.pokedex.framework.database.PokemonDatabase
import com.architect.coders.pokedex.framework.network.PokeService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @ApiUrl
    fun provideApiUrl(): String = "http://localhost:8080"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRemoteClient(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): PokeService {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PokeService::class.java)
    }
}