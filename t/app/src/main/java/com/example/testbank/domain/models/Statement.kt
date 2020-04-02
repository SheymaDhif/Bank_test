package com.example.testbank.domain.models

data class Statement(
    val date: String,
    val desc: String,
    val title: String,
    val value: Double
)