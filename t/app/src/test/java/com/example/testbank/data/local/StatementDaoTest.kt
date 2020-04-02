package com.example.testbank.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testbank.data.DBTest
import com.example.testbank.data.models.statements.Statement
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatementDaoTest : DBTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndGetStatementTest() {

        db.statementDao().insertStatement(Statement(
            id = 1,
            date = "2018-08-15",
            desc = "Conta de luz",
            title = "Pagamento",
            value = -50.0
        ))
        val listDb = db.statementDao().getStatements()
        Assert.assertTrue(1 == listDb.size)
    }

    fun generateStatementsDataList(): List<Statement> {
        return (0..4).map {
            Statement(
                id = 1,
                date = "2018-08-15$it",
                desc = "Conta de luz$it",
                title = "Pagamento$it",
                value = -50.0
            )
        }
    }

    @Test
    fun clearTest() {
        db.userDao().clear()
        val list = db.userDao().getUser()
        Assert.assertTrue(list == null)
    }

}