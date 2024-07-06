package com.example.sportfieldsearcher.ui.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportfieldsearcher.data.database.User
import com.example.sportfieldsearcher.data.repositories.UsersRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UsersState(
    val users: List<User>,
    //val usersWithFields: List<UserWithFields>
)

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {

    val state: StateFlow<UsersState> = repository.users
        .map { users -> UsersState(users = users) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UsersState(emptyList())
        )

    fun addUser(user: User) = viewModelScope.launch { repository.upsert(user) }
    fun updateUser(user: User) = viewModelScope.launch { repository.upsert(user) }
    fun deleteUser(user: User) = viewModelScope.launch { repository.delete(user) }
    fun getUserOnLogin(email: String, password: String) : Deferred<User?> = viewModelScope.async { repository.getUserOnLogin(email, password) }
    fun getUserInfo(userId: Int) : Deferred<User?> = viewModelScope.async { repository.getUserInfo(userId) }
    //fun getUserWithEventsById(userId: Int) : Deferred<UserWithEvents?> = viewModelScope.async { repository.getUserWithEventsById(userId) }
}