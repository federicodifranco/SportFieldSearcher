package com.example.sportfieldsearcher.ui.screens.addfield

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddFieldState(
    val location: String = "",
    val date: String = "",
    val description: String = "",
)

interface AddFieldActions {
    fun setLocation(title: String)
    fun setDate(date: String)
    fun setDescription(description: String)
}

class AddFieldViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddFieldState())
    val state = _state.asStateFlow()

    val actions = object : AddFieldActions {
        override fun setLocation(title: String) =
            _state.update { it.copy(location = title) }

        override fun setDate(date: String) =
            _state.update { it.copy(date = date) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }
    }
}
