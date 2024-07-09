package com.example.sportfieldsearcher.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CategoryType {
    NONE,
    BASKET,
    CALCIO,
    PALLAVOLO,
    TENNIS
}

enum class PrivacyType {
    NONE,
    PUBLIC,
    PRIVATE
}

@Entity
data class Field(
    @PrimaryKey (autoGenerate = true)
    val fieldId: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val date: String,

    @ColumnInfo
    val description:String,

    @ColumnInfo
    val category: CategoryType,

    @ColumnInfo
    val fieldAddedId: Int,

    @ColumnInfo
    val privacyType: PrivacyType,

    @ColumnInfo
    val city: String
)