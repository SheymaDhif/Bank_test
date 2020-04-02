package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class SignInUseCase  @Inject constructor(private val userRepository: UserRepository) {

    fun execute(user: String,password: String): Single<User> {
        return userRepository.signIn(user,password)
    }
}