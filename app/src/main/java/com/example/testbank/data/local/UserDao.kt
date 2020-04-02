package com.example.testbank.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testbank.data.models.signin.UserAccount

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userAccount: UserAccount)

    @Query("DELETE FROM users")
    fun clear()

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): UserAccount?
}