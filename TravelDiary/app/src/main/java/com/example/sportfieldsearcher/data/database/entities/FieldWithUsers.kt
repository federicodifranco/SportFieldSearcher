package com.example.sportfieldsearcher.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FieldWithUsers(
    @Embedded
    val field: Field,

    @Relation(
        parentColumn = "fieldId",
        entityColumn = "userId",
        associateBy = Junction(Connection::class)
    )
    val connection: List<User>
)