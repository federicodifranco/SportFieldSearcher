package com.example.sportfieldsearcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportfieldsearcher.data.database.Field
import com.example.sportfieldsearcher.data.repositories.FieldsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FieldsState(val fields: List<Field>)

class FieldsViewModel(
    private val repository: FieldsRepository
) : ViewModel() {
    val state = repository.fields.map { fields -> FieldsState(fields) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FieldsState(emptyList())
    )

    fun addField(field: Field) = viewModelScope.launch {
        repository.upsert(field)
    }
}