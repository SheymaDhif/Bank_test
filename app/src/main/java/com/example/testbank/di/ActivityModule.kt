package com.example.testbank.di

import com.example.testbank.presentation.statements.StatementsActivity
import com.example.testbank.presentation.auth.SignInActivity
import com.example.testbank.presentation.auth.SignInActivityModule
import com.example.testbank.presentation.statements.StatementsActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [StatementsActivityModule::class])
    internal abstract fun contributeStatementsActivity(): StatementsActivity

    @ContributesAndroidInjector(modules = [SignInActivityModule::class])
    internal abstract fun contributeSignInActivity(): SignInActivity
}