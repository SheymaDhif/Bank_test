package com.example.testbank.domain.usecase

import com.example.testbank.domain.repository.UserRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LogOutUseCaseTest  {

    private lateinit var userRepository: UserRepository
    private lateinit var logOutUseCase: LogOutUseCase

    @Before
    fun before() {
        userRepository = Mockito.mock(UserRepository::class.java)
        logOutUseCase = LogOutUseCase(userRepository)
    }

    @Test
    fun executeTestWhenSuccess() {
        whenever(userRepository.logout()).thenReturn(Completable.complete())
        val test = logOutUseCase.execute().test()
        verify(userRepository).logout()
        test.assertComplete()
    }

    @Test
    fun executeTestWhenFailed() {
        whenever(userRepository.logout()).thenReturn(Completable.error(Throwable("error")))
        val test = logOutUseCase.execute().test()
        verify(userRepository).logout()
        test.assertNotComplete()
    }
}