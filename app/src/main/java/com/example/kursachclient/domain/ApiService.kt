package com.example.kursachclient.domain

import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{

    @GET("api/Book")
    suspend fun getBooks(): List<Book>

    @POST("api/authorize")
    suspend fun getToken(model: AuthorizeModel): TokenModel

    @POST("api/register")
    suspend fun register(model: RegisterModel)

    @GET("api/login-is-free/check")
    suspend fun loginIsFree(login:String)

    @GET("api/user")
    suspend fun getUser(token:String): Any

}