package com.example.sportfieldsearcher

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.sportfieldsearcher.data.database.SportFieldSearcherDatabase
import com.example.sportfieldsearcher.data.remote.OSMDataSource
import com.example.sportfieldsearcher.data.repositories.AppRepository
import com.example.sportfieldsearcher.data.repositories.ConnectionRepository
import com.example.sportfieldsearcher.data.repositories.FieldsRepository
import com.example.sportfieldsearcher.data.repositories.UsersRepository
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.controllers.FieldsViewModel
import com.example.sportfieldsearcher.ui.controllers.UsersViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.login.LoginViewModel
import com.example.sportfieldsearcher.ui.screens.register.RegistrationViewModel
import com.example.sportfieldsearcher.ui.utils.LocationService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

    single {
        FieldsRepository(
            get<SportFieldSearcherDatabase>().fieldsDAO(),
        )
    }

    single {
        ConnectionRepository(
            get<SportFieldSearcherDatabase>().connectionDAO(),
        )
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { AppRepository(get()) }
    single { get<Context>().dataStore }
    single { LocationService(get()) }
    single { OSMDataSource(get()) }

    viewModel { AddFieldViewModel() }
    viewModel { AppViewModel(get()) }
    viewModel { FieldsViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { RegistrationViewModel() }
    viewModel { UsersViewModel(get()) }
}
