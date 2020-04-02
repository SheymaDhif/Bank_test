package com.example.testbank.data.repository

import com.example.testbank.data.local.StatementDao
import com.example.testbank.data.models.statements.Error
import com.example.testbank.data.models.statements.Statement
import com.example.testbank.data.models.statements.StatementResponse
import com.example.testbank.data.remote.RemoteService
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class StatementsRepositoryImplTest {

    private lateinit var statementDao: StatementDao
    private lateinit var remoteService: RemoteService
    private lateinit var statementsRepositoryImpl: StatementsRepositoryImpl
    private val testScheduler = TestScheduler()


    @Before
    fun before() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        statementDao = Mockito.mock(StatementDao::class.java)
        remoteService = Mockito.mock(RemoteService::class.java)
        statementsRepositoryImpl = StatementsRepositoryImpl(statementDao, remoteService)
    }

    @Test
    fun getStatementsTest() {
        val list = listOf<Statement>(Statement(1, "Pagamento", "Conta de luz", "2018-08-15", -50.0))
        whenever(remoteService.getStatements("1")).thenReturn(
            Single.just(
                StatementResponse(
                    Error(),
                    list
                )
            )
        )

        val test = statementsRepositoryImpl.getStatements("1").test()
        testScheduler.triggerActions()
        verify(statementDao).insertAllStatements(list)
        test.assertComplete().assertValue { result-> result.size == 1}

    }


}