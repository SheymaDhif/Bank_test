package com.example.testbank.presentation.auth

import androidx.lifecycle.MutableLiveData
import com.example.testbank.domain.models.User
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.SignInUseCase
import com.example.testbank.extensions.SingleLiveEvent
import com.example.testbank.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject
constructor(
    private val signInUseCase: SignInUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase
) : BaseViewModel() {

    var btnSelected: MutableLiveData<Boolean> = MutableLiveData()
    var showProgress: MutableLiveData<Boolean> = MutableLiveData()
    var userAccount: MutableLiveData<User> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()
    var userExist: MutableLiveData<Boolean> = MutableLiveData()

    init {
        btnSelected.value = false
        showProgress.value = false
    }

    fun getUserDetails() {
        getUserDetailUseCase.execute().subscribe({
            userExist.value = true
        }, {
            userExist.value = false

        })
    }

    fun onChange(user: String, password: String) {
        btnSelected.value = user.isNotEmpty() && password.isNotEmpty()
    }

    fun signIn(userV: String, password: String) {
        showProgress.value = true
        addDisposable(signInUseCase
            .execute(user = userV, password = password)
            .doAfterTerminate { showProgress.value = false }
            .subscribe({
                Timber.d("signIn onSuccess $it")
                userAccount.value = it
                showProgress.value = false

            }, {
                Timber.d("signIn onFailed $it")
                errorState.value = it
                showProgress.value = false
            }
            ))
    }

    fun unbound() {
        super.onCleared()
    }
}