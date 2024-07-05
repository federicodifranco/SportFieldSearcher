package com.example.sportfieldsearcher.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.example.sportfieldsearcher.data.models.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingState(val theme: Theme)

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingState(Theme.System))
    val state = _state.asStateFlow()

    fun changeTheme(theme: Theme) {
        _state.value = SettingState(theme)
    }
}