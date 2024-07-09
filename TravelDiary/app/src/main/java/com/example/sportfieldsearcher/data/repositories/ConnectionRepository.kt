package com.example.sportfieldsearcher.data.repositories

import com.example.sportfieldsearcher.data.database.daos.ConnectionDAO
import com.example.sportfieldsearcher.data.database.entities.Connection
import kotlinx.coroutines.flow.Flow

class ConnectionRepository(
    private val connectionDAO: ConnectionDAO
) {
    val connections: Flow<List<Connection>> = connectionDAO.getAll()

    suspend fun upsert(connection: Connection) = connectionDAO.upsert(connection)
    suspend fun delete(connection: Connection) = connectionDAO.delete(connection)
}