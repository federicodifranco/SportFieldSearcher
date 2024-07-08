package com.example.sportfieldsearcher

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.sportfieldsearcher.data.database.SportFieldSearcherDatabase
import com.example.sportfieldsearcher.data.repositories.AppRepository
import com.example.sportfieldsearcher.data.repositories.FieldsRepository
import com.example.sportfieldsearcher.data.repositories.SettingsRepository
import com.example.sportfieldsearcher.data.repositories.UsersRepository
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.controllers.FieldsViewModel
import com.example.sportfieldsearcher.ui.controllers.UsersViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.login.LoginViewModel
import com.example.sportfieldsearcher.ui.screens.register.RegistrationViewModel
import com.example.sportfieldsearcher.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            SportFieldSearcherDatabase::class.java,
            "Sport-Field"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        UsersRepository(
            get<SportFieldSearcherDatabase>().usersDAO(),
        )
    }

    single { get<Context>().dataStore }
    single { SettingsRepository(get()) }
    single { AppRepository(get()) }
    single { FieldsRepository(get<SportFieldSearcherDatabase>().fieldsDAO())}

    viewModel { AddFieldViewModel() }
    viewModel { RegistrationViewModel() }
    viewModel { LoginViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { FieldsViewModel(get()) }
    viewModel { AppViewModel(get()) }
    viewModel { UsersViewModel(get()) }
}
