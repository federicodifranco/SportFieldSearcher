package com.example.sportfieldsearcher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sportfieldsearcher.data.database.daos.ConnectionDAO
import com.example.sportfieldsearcher.data.database.daos.FieldsDAO
import com.example.sportfieldsearcher.data.database.daos.UsersDAOs
import com.example.sportfieldsearcher.data.database.entities.Connection
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.User

@Database(entities = [Field::class, User::class, Connection::class], version = 5)
abstract class SportFieldSearcherDatabase : RoomDatabase(){
    abstract fun fieldsDAO() : FieldsDAO
    abstract  fun usersDAO() : UsersDAOs
    abstract fun connectionDAO() : ConnectionDAO
}