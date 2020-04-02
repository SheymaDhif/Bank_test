package com.example.testbank.data.models.statements

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statements")
data class Statement(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String,
    val desc: String,
    val title: String,
    val value: Double
)