package com.example.testbank.domain.models

data class User (
    val agency: String,
    val balance: Double,
    val bankAccount: String,
    val name: String,
    val userId: Int
)