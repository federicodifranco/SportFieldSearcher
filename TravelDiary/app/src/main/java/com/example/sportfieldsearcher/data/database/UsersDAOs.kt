package com.example.sportfieldsearcher.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface  UsersDAOs {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAll(): Flow<List<User>>

    @Upsert
    suspend fun upsert(user: User)

    @Delete
    suspend fun delete(item: User)
}