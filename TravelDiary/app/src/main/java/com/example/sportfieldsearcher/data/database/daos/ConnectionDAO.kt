package com.example.sportfieldsearcher.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import com.example.sportfieldsearcher.data.database.entities.Connection

@Dao
interface ConnectionDAO {
    @Query("SELECT * FROM connection ORDER BY userId ASC")
    fun getAll(): Flow<List<Connection>>

    @Upsert
    suspend fun upsert(connection: Connection)

    @Delete
    suspend fun delete(connection: Connection)
}