package com.example.testbank.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testbank.data.local.StatementDao
import com.example.testbank.data.local.UserDao
import com.example.testbank.data.models.signin.UserAccount
import com.example.testbank.data.models.statements.Statement

@Database(
    entities = arrayOf(UserAccount::class, Statement::class),
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun statementDao(): StatementDao

}

