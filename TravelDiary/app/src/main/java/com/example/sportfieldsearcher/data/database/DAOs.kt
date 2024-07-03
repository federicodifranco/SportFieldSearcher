package com.example.sportfieldsearcher.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldsDAO {
    @Query("SELECT * FROM field ORDER BY name ASC")
    fun getAll(): Flow<List<Field>>

    @Upsert
    suspend fun upsert(field: Field)

    @Delete
    suspend fun delete(field: Field)
}