package com.example.testbank.data.models.statements

data class StatementResponse(
    val error: Error,
    val statementList: List<Statement>
)