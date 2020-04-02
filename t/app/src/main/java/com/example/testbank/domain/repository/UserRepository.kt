package com.example.testbank.domain.repository

import com.example.testbank.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun signIn(user: String, password: String): Single<User>
    fun getUserId(): Single<Int>
    fun getUser(): Single<User>
    fun isConnected(): Single<Boolean>
    fun logout(): Completable
}