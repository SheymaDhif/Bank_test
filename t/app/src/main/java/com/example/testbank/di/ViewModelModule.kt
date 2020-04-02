package com.example.testbank.di

import androidx.lifecycle.ViewModel
import com.example.testbank.presentation.auth.SignInViewModel
import com.example.testbank.presentation.statements.StatementsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInActivityViewModel(signInActivityViewModel: SignInViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(StatementsViewModel::class)
    internal abstract fun bindStatementsViewModel(statementsViewModel: StatementsViewModel): ViewModel

}