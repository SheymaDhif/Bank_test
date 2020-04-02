package com.example.testbank.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testbank.data.DBTest
import com.example.testbank.data.models.signin.UserAccount
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoImplTest : DBTest(){


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndGetUserTest() {
        db.userDao().insertUser(UserAccount(agency = "012314564",balance=3.3445,bankAccount = "1234567890",name = "Jose da Silva Teste",userId = 1))
        val user = db.userDao().getUser()
        MatcherAssert.assertThat(user?.name, CoreMatchers.`is`("Jose da Silva Teste"))
    }

    @Test
    fun clearTest() {
        db.userDao().clear()
        val list = db.userDao().getUser()
        Assert.assertTrue(list == null)
    }
}