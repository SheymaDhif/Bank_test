package com.example.testbank.domain.usecase

import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun execute(): Single<User> {
        return userRepository.getUser()
    }
}