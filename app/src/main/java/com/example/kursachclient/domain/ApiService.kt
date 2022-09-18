package com.example.kursachclient.domain

import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{

    @GET("api/Book")
    suspend fun getBooks(): List<Book>

//    @POST("api/book")
//    fun postBook()
}