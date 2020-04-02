package com.example.testbank.data.remote

import com.example.testbank.data.models.signin.SignInResponse
import com.example.testbank.data.models.statements.StatementResponse
import io.reactivex.Single
import retrofit2.http.*

interface RemoteService {

    @FormUrlEncoded
    @POST("api/login")
    fun signIn(@Field("user") user: String, @Field("password") password: String): Single<SignInResponse>

    @GET("api/statements/{idUser}")
    fun getStatements(@Path("idUser") idUser: String): Single<StatementResponse>
}