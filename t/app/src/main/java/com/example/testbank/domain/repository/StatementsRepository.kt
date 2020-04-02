package com.example.testbank.domain.repository

import com.example.testbank.domain.models.Statement
import io.reactivex.Single

interface StatementsRepository {
    fun getStatements(idUser: String): Single<List<Statement>>
}