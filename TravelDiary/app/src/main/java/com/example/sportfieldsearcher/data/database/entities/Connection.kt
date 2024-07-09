package com.example.sportfieldsearcher.data.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "fieldId"])
data class Connection(
    val userId: Int,
    val fieldId: Int
)