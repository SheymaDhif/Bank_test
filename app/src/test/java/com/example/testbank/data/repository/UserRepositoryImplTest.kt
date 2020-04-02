package com.example.testbank.data.repository

import com.example.testbank.data.local.UserDao
import com.example.testbank.data.models.signin.Error
import com.example.testbank.data.models.signin.SignInResponse
import com.example.testbank.data.models.signin.UserAccount
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

class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var remoteService: RemoteService
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private val testScheduler = TestScheduler()
    private val userAccount = UserAccount("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)

    @Before
    fun before() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        userDao = Mockito.mock(UserDao::class.java)
        remoteService = Mockito.mock(RemoteService::class.java)
        userRepositoryImpl = UserRepositoryImpl(userDao, remoteService)
    }

    @Test
    fun signInTest() {
        whenever(
            remoteService.signIn(
                "test_user",
                "Test@1"
            )
        ).thenReturn(Single.just(SignInResponse(Error(),userAccount)))

        val test = userRepositoryImpl.signIn("test_user", "Test@1").test()
        testScheduler.triggerActions()
        verify(userDao).insertUser(userAccount)
        test.assertComplete().assertValue { result-> result.agency.equals("012314564")  && result.balance== 3.3445 && result.name.equals("Jose da Silva Teste") && result.userId == 1}
    }

    @Test
    fun getUserIdTest() {
        whenever(userDao.getUser()).thenReturn(userAccount)
        val test = userRepositoryImpl.getUserId().test()
        verify(userDao).getUser()
        test.assertComplete().assertValue { result->  result == userAccount.userId}
    }

    @Test
    fun getUserWhenCacheIsNotEmpty() {
        whenever(userDao.getUser()).thenReturn(userAccount)
        val test = userRepositoryImpl.getUser().test()
        verify(userDao).getUser()
        test.assertComplete().assertValue { result-> result.agency.equals("012314564")  && result.balance== 3.3445 && result.name.equals("Jose da Silva Teste") && result.userId == 1 }
    }

    @Test
    fun getUserWhenCacheIsEmpty() {
        whenever(userDao.getUser()).thenReturn(null)
        val test = userRepositoryImpl.getUser().test()
        testScheduler.triggerActions()

        verify(userDao).getUser()
        test.assertNotComplete()
    }

    @Test
    fun isConnectedWhenUserExist() {
        whenever(userDao.getUser()).thenReturn(userAccount)
        val test = userRepositoryImpl.isConnected().test()
        verify(userDao).getUser()
        test.assertComplete().assertValue { result-> result }
    }

    @Test
    fun isConnectedWhenUserIsNotExist() {
        whenever(userDao.getUser()).thenReturn(null)
        val test = userRepositoryImpl.isConnected().test()
        verify(userDao).getUser()
        test.assertComplete().assertValue { result-> !result }
    }

    @Test
    fun logOutTest() {
        userRepositoryImpl.logout().test().assertComplete()
        verify(userDao).clear()
    }


    }