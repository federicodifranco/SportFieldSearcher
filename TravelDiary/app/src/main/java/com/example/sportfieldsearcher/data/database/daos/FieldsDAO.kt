package com.example.sportfieldsearcher.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldsDAO {
    @Query("SELECT * FROM field ORDER BY name ASC")
    fun getAll(): Flow<List<Field>>

    @Upsert
    suspend fun upsert(field: Field)

    @Delete
    suspend fun delete(field: Field)

    @Transaction
    @Query("SELECT * FROM field")
    fun getFieldsWithUsers(): Flow<List<FieldWithUsers>>
}