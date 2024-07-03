package com.example.sportfieldsearcher.data.repositories

import com.example.sportfieldsearcher.data.database.Field
import com.example.sportfieldsearcher.data.database.FieldsDAO

class FieldsRepository(
    private val fieldsDAO: FieldsDAO
) {
    val fields = fieldsDAO.getAll()
    suspend fun upsert(field: Field) = fieldsDAO.upsert(field)
    suspend fun delete(field: Field) = fieldsDAO.delete(field)
}