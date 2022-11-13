package com.example.kursachclient.domain

import com.example.kursachclient.domain.model.book.GetBookResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService{

    @GET("api/Book")
    suspend fun getBooks(@Header("Authorization") token: String): Response<List<GetBookResponse>>

    @POST("api/Authorization")
    suspend fun getToken(@Body model: AuthorizeModel): Response<TokenModel>

    @Headers("Content-Type: application/json")
    @POST("api/Registration")
    suspend fun register(@Body model: RegisterModel) : Response<Boolean>

    @GET("api/login-is-free/check")
    suspend fun loginIsFree(login:String) : Response<Any>

    @GET("api/user")
    suspend fun getUser(token:String): Any

}