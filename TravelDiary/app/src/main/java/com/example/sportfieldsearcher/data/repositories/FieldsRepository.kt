package com.example.sportfieldsearcher.data.repositories

import com.example.sportfieldsearcher.data.database.daos.FieldsDAO
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FieldsRepository(
    private val fieldsDAO: FieldsDAO
) {
    val fields: Flow<List<Field>> = fieldsDAO.getAll()
    val fieldsWithUsers: Flow<List<FieldWithUsers>> = fieldsDAO.getFieldsWithUsers()

    suspend fun upsert(field: Field) = fieldsDAO.upsert(field)
    suspend fun delete(field: Field) = fieldsDAO.delete(field)
    suspend fun getFieldsWithUsers() : List<FieldWithUsers> = fieldsWithUsers.first()
    suspend fun getFieldWithUsersById(fieldId: Int) : FieldWithUsers? = fieldsWithUsers.first().find { it.field.fieldId == fieldId }
}