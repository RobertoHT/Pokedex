package com.architect.coders.pokedex

import android.app.Application
import androidx.room.Room
import com.architect.coders.pokedex.database.PokemonDatabase

class App : Application() {

    lateinit var db: PokemonDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            PokemonDatabase::class.java,
            "pokemon-db"
        ).build()
    }
}