package com.example.testbank.data.models.signin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserAccount(
    val agency: String,
    val balance: Double,
    val bankAccount: String,
    val name: String,
    @PrimaryKey
    val userId: Int
)