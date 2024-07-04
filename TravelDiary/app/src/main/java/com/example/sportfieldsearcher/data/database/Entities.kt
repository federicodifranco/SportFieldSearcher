package com.example.sportfieldsearcher.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Field(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val date: String,

    @ColumnInfo
    val description:String,
)