package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SignInUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun before() {
        userRepository = mock(UserRepository::class.java)
        signInUseCase = SignInUseCase(userRepository)
    }

    @Test
    fun executeTest() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.signIn("test_user", "Test@1")).thenReturn(Single.just(user))
        signInUseCase.execute("test_user", "Test@1")
        verify(userRepository).signIn("test_user", "Test@1")
    }


}