package com.architect.coders.pokedex.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.architect.coders.pokedex.di.ColorSwatch
import com.architect.coders.pokedex.di.PokemonId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @PokemonId
    fun providePokemonId(savedStateHandle: SavedStateHandle) =
        DetailFragmentArgs.fromSavedStateHandle(savedStateHandle).id

    @Provides
    @ViewModelScoped
    @ColorSwatch
    fun provideColorSwatch(savedStateHandle: SavedStateHandle) =
        DetailFragmentArgs.fromSavedStateHandle(savedStateHandle).colorSwatch
}