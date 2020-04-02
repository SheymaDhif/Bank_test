package com.example.testbank.domain.usecase

import com.example.testbank.domain.repository.UserRepository
import io.reactivex.Completable
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun execute(): Completable {
        return userRepository.logout()
    }
}