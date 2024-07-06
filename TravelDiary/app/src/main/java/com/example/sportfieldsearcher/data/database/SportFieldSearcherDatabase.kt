package com.example.sportfieldsearcher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Field::class, User::class], version = 2)
abstract class SportFieldSearcherDatabase : RoomDatabase(){
    abstract fun fieldsDAO() : FieldsDAO
    abstract  fun usersDAO() : UsersDAOs
}