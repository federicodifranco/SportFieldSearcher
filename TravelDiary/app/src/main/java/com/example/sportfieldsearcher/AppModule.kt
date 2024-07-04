package com.example.sportfieldsearcher

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sportfieldsearcher.data.database.SportFieldSearcherDatabase
import com.example.sportfieldsearcher.data.repositories.FieldsRepository
import com.example.sportfieldsearcher.data.repositories.SettingsRepository
import com.example.sportfieldsearcher.ui.FieldsViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldScreen
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            SportFieldSearcherDatabase::class.java,
            "Sport-Field"
        ).build()
    }

    single { SettingsRepository(get()) }

    single { FieldsRepository(get<SportFieldSearcherDatabase>().fieldsDAO())}

    viewModel { AddFieldViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { FieldsViewModel(get()) }
}
