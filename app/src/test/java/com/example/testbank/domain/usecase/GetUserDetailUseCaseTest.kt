package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetUserDetailUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getUserDetailUseCase: GetUserDetailUseCase

    @Before
    fun before() {
        userRepository = Mockito.mock(UserRepository::class.java)
        getUserDetailUseCase = GetUserDetailUseCase(userRepository)
    }

    @Test
    fun executeTestWhenSuccess() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.getUser()).thenReturn(Single.just(user))
        val test = getUserDetailUseCase.execute().test()
        verify(userRepository).getUser()
        test.assertComplete()

    }

    @Test
    fun executeTestWhenFailed() {
        whenever(userRepository.getUser()).thenReturn(Single.error(Throwable("error")))
        val test = getUserDetailUseCase.execute().test()
        verify(userRepository).getUser()
        test.assertNotComplete()

    }
}