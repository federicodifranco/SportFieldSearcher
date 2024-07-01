package com.example.traveldiary

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.traveldiary.data.repositories.SettingsRepository
import com.example.traveldiary.ui.screens.addtravel.AddTravelViewModel
import com.example.traveldiary.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single { SettingsRepository(get()) }

    viewModel { AddTravelViewModel() }

    viewModel { SettingsViewModel(get()) }
}
