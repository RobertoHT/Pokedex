package com.architect.coders.pokedex.ui.collection

import androidx.lifecycle.SavedStateHandle
import com.architect.coders.pokedex.di.PokemonImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class CollectionViewModelModule {

    @Provides
    @ViewModelScoped
    @PokemonImage
    fun providePokemonImage(savedStateHandle: SavedStateHandle) =
        CollectionFragmentArgs.fromSavedStateHandle(savedStateHandle).imagePath
}