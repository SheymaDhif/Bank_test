package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.Statement
import com.example.testbank.domain.repository.StatementsRepository
import com.example.testbank.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class GetStatementsUseCase @Inject constructor(
    private val statementsRepository: StatementsRepository
) {
    fun execute(idUser: String): Single<List<Statement>> {
        return statementsRepository.getStatements(idUser)
    }
}