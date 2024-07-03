package com.example.sportfieldsearcher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Field::class], version = 1)
abstract class SportFieldSearcherDatabase : RoomDatabase(){
    abstract fun fieldsDAO() : FieldsDAO
}