package com.example.testbank.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.testbank.data.AppDatabase
import com.example.testbank.data.local.StatementDao
import com.example.testbank.data.local.UserDao
import com.example.testbank.data.remote.RemoteService
import com.example.testbank.data.repository.StatementsRepositoryImpl
import com.example.testbank.data.repository.UserRepositoryImpl
import com.example.testbank.domain.repository.StatementsRepository
import com.example.testbank.domain.repository.UserRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(app: Application): Context = app

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://bank-app-test.herokuapp.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteService(@NonNull retrofit: Retrofit): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "database.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(@NonNull database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideStatementDao(@NonNull database: AppDatabase): StatementDao {
        return database.statementDao()
    }

    @Provides
    @Singleton
    fun provideUserRepositoryImpl(
        userDao: UserDao,
        remoteService: RemoteService
    ): UserRepository {
        return UserRepositoryImpl(userDao, remoteService)
    }

    @Provides
    @Singleton
    fun provideStatementsRepositoryImpl(
        statementDao: StatementDao,
        remoteService: RemoteService
    ): StatementsRepository {
        return StatementsRepositoryImpl(statementDao, remoteService)
    }


}