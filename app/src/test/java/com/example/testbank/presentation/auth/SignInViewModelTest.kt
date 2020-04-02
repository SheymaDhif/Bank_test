package com.example.testbank.presentation.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testbank.domain.models.User
import com.example.testbank.domain.repository.UserRepository
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.SignInUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    private val userRepository: UserRepository = mock()
    private val signInUseCase = SignInUseCase(userRepository)
    private var getUserDetailUseCase: GetUserDetailUseCase= GetUserDetailUseCase(userRepository)
    private val testScheduler = TestScheduler()
    private lateinit var signInViewModel: SignInViewModel
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun before() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        getUserDetailUseCase = GetUserDetailUseCase(userRepository)
        signInViewModel = SignInViewModel(signInUseCase, getUserDetailUseCase)
    }




    @Test
    fun initTest() {
        assert(signInViewModel.btnSelected.value == false)
        assert(signInViewModel.showProgress.value == false)
    }


    @Test
    fun getDetailUserDetailSuccess() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.getUser()).thenReturn(Single.just(user))
        signInViewModel.getUserDetails()
        testScheduler.triggerActions()
        assert(signInViewModel.userExist.value == true)
    }

    @Test
    fun getDetailUserDetailFailed() {
        whenever(userRepository.getUser()).thenReturn(Single.error(RuntimeException("user is null")))
        signInViewModel.getUserDetails()
        testScheduler.triggerActions()
        assert(signInViewModel.userExist.value == false)
    }

    @Test
    fun onChangeWhenUserAndPasswordIsEmpty() {
        signInViewModel.onChange("","")
        assert(signInViewModel.btnSelected.value == false)
    }

    @Test
    fun onChangeWhenUserAndPasswordNotIsEmpty() {
        signInViewModel.onChange("test","test")
        assert(signInViewModel.btnSelected.value == true)
    }

    @Test
    fun onChangeWhenUserIsEmpty() {
        signInViewModel.onChange("","test")
        assert(signInViewModel.btnSelected.value == false)
    }

    @Test
    fun onChangeWhenPasswordIsEmpty() {
        signInViewModel.onChange("test","")
        assert(signInViewModel.btnSelected.value == false)
    }

    @Test
    fun signInSuccess() {
        val user = User("012314564", 3.3445, "2050", "Jose da Silva Teste", 1)
        whenever(userRepository.signIn("test_user", "Test@1")).thenReturn(Single.just(user))
        signInViewModel.signIn("test_user", "Test@1")
        testScheduler.triggerActions()
        assert(signInViewModel.userAccount.value == user)
        assert(signInViewModel.showProgress.value == false)
    }

    @Test
    fun signInFailed() {
        whenever(userRepository.signIn("test_user", "Test@1")).thenReturn(Single.error(Throwable("")))
        signInViewModel.signIn("test_user", "Test@1")
        testScheduler.triggerActions()
        AssertionError(signInViewModel.errorState.value == Throwable(""))
        assert(signInViewModel.showProgress.value == false)
    }
}