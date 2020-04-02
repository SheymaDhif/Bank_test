package com.example.testbank.presentation.statements

import androidx.lifecycle.ViewModelProvider
import com.example.testbank.domain.usecase.GetStatementsUseCase
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.LogOutUseCase
import com.example.testbank.presentation.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class StatementsActivityModule {

    @Provides
    internal fun providesViewModel(
        statementsUseCase: GetStatementsUseCase,
        getUserDetailUseCase: GetUserDetailUseCase,
        logOutUseCase: LogOutUseCase
    ): StatementsViewModel {
        return StatementsViewModel(statementsUseCase, getUserDetailUseCase, logOutUseCase)
    }

    @Provides
    internal fun provideViewModelProvider(viewModel: StatementsViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }

}