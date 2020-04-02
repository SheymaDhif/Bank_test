package com.example.testbank.presentation.auth

import androidx.lifecycle.ViewModelProvider
import com.example.testbank.domain.usecase.GetUserDetailUseCase
import com.example.testbank.domain.usecase.SignInUseCase
import com.example.testbank.presentation.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class SignInActivityModule {

    @Provides
    internal fun providesViewModel(
        signInUseCase: SignInUseCase,
        getUserDetailUseCase: GetUserDetailUseCase
    ): SignInViewModel {
        return SignInViewModel(signInUseCase, getUserDetailUseCase)
    }

    @Provides
    internal fun provideViewModelProvider(viewModel: SignInViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}