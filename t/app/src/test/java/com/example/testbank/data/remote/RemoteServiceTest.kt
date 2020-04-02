package com.example.testbank.data.remote

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.testbank.data.models.signin.Error
import com.example.testbank.data.models.signin.SignInResponse
import com.example.testbank.data.models.signin.UserAccount
import com.example.testbank.data.models.statements.Statement
import com.example.testbank.data.models.statements.StatementResponse
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.Charset

@RunWith(JUnit4::class)
class RemoteServiceTest {


    private lateinit var webService: RemoteService
    private lateinit var mockWebServer: MockWebServer

    @Throws(IOException::class)
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        webService = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .build()
            .create(RemoteService::class.java)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun isMainThread() = true
            override fun postToMainThread(runnable: Runnable) = runnable.run()
        })
    }

    @Test
    fun signInTest() {
        val content =
            this.javaClass.classLoader!!.getResource("mock/sign-in-response.json")
                .readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(content)
        )
        val signinResponse = SignInResponse(
            Error(),
            UserAccount(
                agency = "012314564",
                balance = 3.3445,
                bankAccount = "2050",
                name = "Jose da Silva Teste",
                userId = 1
            )
        )

        val response = webService.signIn("test_user", "Test@1").blockingGet()
        Assert.assertEquals(response.userAccount, signinResponse.userAccount)
    }


    @Test
    fun getStatements() {
        val content =
            this.javaClass.classLoader!!.getResource("mock/get-statements-response.json")
                .readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(content)
        )

        val statements = listOf<Statement>(Statement(1,"Pagamento","Conta de luz","2018-08-15",-50.0))
        val statementResponse = StatementResponse(
            com.example.testbank.data.models.statements.Error(),
            statements
        )

        val response = webService.getStatements("1").blockingGet()
        Assert.assertEquals(response.statementList.size, statementResponse.statementList.size)
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


}