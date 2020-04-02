package com.example.testbank.presentation.statements

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testbank.domain.models.Statement
import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.StatementsRepository
import com.example.testbank.domain.repository.UserRepository
import com.example.testbank.domain.usecase.GetStatementsUseCase
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.LogOutUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatementsViewModelTest {

    private val userRepository: UserRepository = mock()
    private val statementsRepository: StatementsRepository = mock()
    private val getUserDetailUseCase: GetUserDetailUseCase = GetUserDetailUseCase(userRepository)
    private val logOutUseCase = LogOutUseCase(userRepository)
    private val getStatementsUseCase: GetStatementsUseCase = GetStatementsUseCase(statementsRepository)
    private lateinit var statementsViewModel: StatementsViewModel
    private val testScheduler = TestScheduler()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        statementsViewModel = StatementsViewModel(getStatementsUseCase, getUserDetailUseCase,logOutUseCase)
    }

    @Test
    fun initTest() {
        assert(statementsViewModel.logOutMutable.value == false)
    }

    @Test
    fun getUserSuccess() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.getUser()).thenReturn(Single.just(user))
        statementsViewModel.getUser()
        testScheduler.triggerActions()
        assert(statementsViewModel.userMutable.value == user)
    }

    @Test
    fun getUserFailed() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.getUser()).thenReturn(Single.error(Throwable("")))
        statementsViewModel.getUser()
        testScheduler.triggerActions()
        AssertionError(statementsViewModel.errorState.value == Throwable(""))
    }

    @Test
    fun getStatementsSuccess() {
        val statements = listOf(Statement("Pagamento","Conta de luz","2018-08-15",-50.0))
        whenever(statementsRepository.getStatements(any())).thenReturn(Single.just(statements))
        statementsViewModel.getStatements("1")
        testScheduler.triggerActions()
        assert(statementsViewModel.statementsMutable.value == statements)
    }

    @Test
    fun getStatementsFailed() {
        whenever(statementsRepository.getStatements(any())).thenReturn(Single.error(Throwable("")))
        statementsViewModel.getStatements("1")
        testScheduler.triggerActions()
        AssertionError(statementsViewModel.errorState.value == Throwable(""))
    }

    @Test
    fun logOutSuccess() {
        whenever(userRepository.logout()).thenReturn(Completable.complete())
        statementsViewModel.logout()
        testScheduler.triggerActions()
        assert(statementsViewModel.logOutMutable.value == true)
    }

    @Test
    fun logOutFailed() {
        whenever(userRepository.logout()).thenReturn(Completable.error(Throwable("")))
        statementsViewModel.logout()
        testScheduler.triggerActions()
        assert(statementsViewModel.logOutMutable.value == false)
    }

}