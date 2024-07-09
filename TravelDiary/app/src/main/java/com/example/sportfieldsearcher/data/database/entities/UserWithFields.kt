package com.example.sportfieldsearcher.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFields(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "userId",
        entityColumn = "fieldId",
        associateBy = Junction(Connection::class)
    )
    val fields: List<Field>,

    @Relation(
        parentColumn = "userId",
        entityColumn = "fieldAddedId"
    )
    val createdFields: List<Field>,
)