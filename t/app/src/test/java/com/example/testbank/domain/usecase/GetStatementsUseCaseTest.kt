package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.Statement
import com.example.testbank.domain.repository.StatementsRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetStatementsUseCaseTest {

    private lateinit var statementsRepository: StatementsRepository
    private lateinit var getStatementsUseCase: GetStatementsUseCase

    @Before
    fun before() {
        statementsRepository = mock()
        getStatementsUseCase = GetStatementsUseCase(statementsRepository)
    }

    @Test
    fun executeTestWhenSuccess() {
        val statements = listOf(Statement("Pagamento","Conta de luz","2018-08-15",-50.0))
        whenever(statementsRepository.getStatements(any())).thenReturn(Single.just(statements))
        val test = getStatementsUseCase.execute("1").test()
        verify(statementsRepository).getStatements("1")
        test.assertComplete()

    }

    @Test
    fun executeTestWhenFailed() {
        whenever(statementsRepository.getStatements("1")).thenReturn(Single.error(Throwable("error")))
        val test = getStatementsUseCase.execute("1").test()
        verify(statementsRepository).getStatements("1")
        test.assertNotComplete()

    }


}