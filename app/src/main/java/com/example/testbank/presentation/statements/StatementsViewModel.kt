package com.example.testbank.presentation.statements

import androidx.lifecycle.MutableLiveData
import com.example.testbank.domain.models.Statement
import com.example.testbank.domain.models.User
import com.example.testbank.domain.usecase.GetStatementsUseCase
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.LogOutUseCase
import com.example.testbank.extensions.SingleLiveEvent
import com.example.testbank.extensions.withDefaultSchedulers
import com.example.testbank.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class StatementsViewModel @Inject
constructor(
    private val getStatementsUseCase: GetStatementsUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel() {

    var userMutable: MutableLiveData<User> = MutableLiveData()
    var logOutMutable: MutableLiveData<Boolean> = MutableLiveData()
    var statementsMutable: MutableLiveData<List<Statement>> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        logOutMutable.value = false
    }

    fun getUser() {
        addDisposable(getUserDetailUseCase.execute().withDefaultSchedulers().subscribe({
            Timber.d("getUserDetailUseCase onSuccess $it")
            userMutable.value = it
            getStatements(it.userId.toString())
        }, {
            Timber.d("getUserDetailUseCase onFailed $it")
            errorState.value = it
        }))
    }

    fun getStatements(idUser: String) {
        addDisposable(getStatementsUseCase.execute(idUser).withDefaultSchedulers().subscribe({
            Timber.d("getStatementsUseCase onSuccess $it")
            statementsMutable.value = it

        }, {
            Timber.d("getStatementsUseCase onFailed $it")
            errorState.value = it
        }))
    }

    fun logout() {
        logOutUseCase.execute().subscribe({
            // handle the completion server has update the user object
            logOutMutable.value = true

        }, {
            //handle error
            errorState.value = it
        })
    }


    fun unbound() {
        super.onCleared()
    }
}