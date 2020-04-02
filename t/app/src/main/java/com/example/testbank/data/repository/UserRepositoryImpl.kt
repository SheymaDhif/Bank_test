package com.example.testbank.data.repository

import com.example.testbank.data.local.UserDao
import com.example.testbank.data.remote.RemoteService
import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import com.example.testbank.extensions.withDefaultSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject
constructor(
    private val userDao: UserDao,
    private val remoteService: RemoteService
) : UserRepository {

    override fun signIn(user: String, password: String): Single<User> {
        userDao.clear()
        return remoteService.signIn(user, password)
            .withDefaultSchedulers()
            .map {
                userDao.insertUser(userAccount = it.userAccount)
                User(
                    it.userAccount.agency,
                    it.userAccount.balance,
                    it.userAccount.bankAccount,
                    it.userAccount.name,
                    it.userAccount.userId
                )
            }
    }

    override fun getUserId(): Single<Int> {
        return Single.just(userDao.getUser()?.userId)
    }

    override fun getUser(): Single<User> {
        val userAccount = userDao.getUser()
        if (userAccount != null) {
            return Single.just(
                User(
                    userAccount.agency,
                    userAccount.balance,
                    userAccount.bankAccount,
                    userAccount.name,
                    userAccount.userId
                )
            )
        } else {
            return Single.error(Throwable("user is null"))
        }
    }

    override fun isConnected(): Single<Boolean> {
        return if (userDao.getUser() != null)
            Single.just(true)
        else
            Single.just(false)
    }

    override fun logout(): Completable {
        return Completable.fromAction {
            userDao.clear()
            Completable.complete()
        }
    }

}