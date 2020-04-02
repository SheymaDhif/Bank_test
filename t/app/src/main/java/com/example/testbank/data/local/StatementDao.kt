package com.example.testbank.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testbank.data.models.statements.Statement

@Dao
interface StatementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatement(statement: Statement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStatements(statements: List<Statement>)

    @Query("DELETE FROM statements")
    fun clear()

    @Query("SELECT * FROM statements")
    fun getStatements(): List<Statement>
}