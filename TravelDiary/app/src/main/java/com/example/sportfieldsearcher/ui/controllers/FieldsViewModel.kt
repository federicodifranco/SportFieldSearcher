package com.example.sportfieldsearcher.ui.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers
import com.example.sportfieldsearcher.data.repositories.FieldsRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FieldsState(
    val fields: List<Field>,
    val fieldsWithUsers: List<FieldWithUsers>
)

class FieldsViewModel(
    private val repository: FieldsRepository
) : ViewModel() {
    val state = repository.fields.combine(repository.fieldsWithUsers) { field: List<Field>, fieldWithUsers: List<FieldWithUsers> ->
        Pair(field, fieldWithUsers)
    }.map { values: Pair<List<Field>, List<FieldWithUsers>> ->
        FieldsState(fields = values.first, fieldsWithUsers = values.second)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FieldsState(emptyList(), emptyList())
    )

    fun addField(field: Field) = viewModelScope.launch { repository.upsert(field) }
    fun updateField(field: Field) = viewModelScope.launch { repository.upsert(field) }
    fun deleteField(field: Field) = viewModelScope.launch { repository.delete(field) }
    fun getFieldWithUsersById(fieldId: Int) : Deferred<FieldWithUsers?> = viewModelScope.async { repository.getFieldWithUsersById(fieldId) }
}