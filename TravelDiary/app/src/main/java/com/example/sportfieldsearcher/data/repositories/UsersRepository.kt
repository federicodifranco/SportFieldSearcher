package com.example.sportfieldsearcher.data.repositories

import com.example.sportfieldsearcher.data.database.daos.UsersDAOs
import com.example.sportfieldsearcher.data.database.entities.User
import com.example.sportfieldsearcher.data.database.entities.UserWithFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UsersRepository(
    private val userDAO: UsersDAOs
) {
    val users: Flow<List<User>> = userDAO.getAll()
    val usersWithFields: Flow<List<UserWithFields>> = userDAO.getUsersWithFields()

    suspend fun upsert(user: User) = userDAO.upsert(user)
    suspend fun delete(user: User) = userDAO.delete(user)
    suspend fun getUserOnLogin(email: String, password: String) : User? {
        return try {
            users.first().first { it.email == email && it.password == password }
        } catch (exception: NoSuchElementException) {
            null
        }
    }
    suspend fun getUserInfo(userId: Int) : User? {
        return try {
            users.first().first { it.userId == userId }
        } catch (exception: NoSuchElementException) {
            null
        }
    }
    suspend fun getUsersWithFields() : List<UserWithFields> = usersWithFields.first()
    suspend fun getUserWithFieldsById(userId: Int) : UserWithFields? {
        return try {
            usersWithFields.first().first { it.user.userId == userId }
        } catch (exception: NoSuchElementException) {
            null
        }
    }
}