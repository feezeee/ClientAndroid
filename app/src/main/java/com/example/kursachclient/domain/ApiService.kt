package com.example.kursachclient.domain

import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService{

    @GET("api/Book")
    suspend fun getBooks(@Query(value="filterName", encoded=true) filterName : String?, @Header("Authorization") token: String): Response<List<GetBookResponse>>

    @POST("api/Book")
    suspend fun addBook(@Header("Authorization") token: String, @Body book: AddBookRequest): Response<Any>

    @PUT("api/Book")
    suspend fun updateBook(@Header("Authorization") token: String, @Body book: UpdateBookRequest): Response<Any>

    @DELETE("api/Book")
    suspend fun deleteBook(@Header("Authorization") token: String, @Query("key") id: Int): Response<Any>



    @POST("api/Authorization")
    suspend fun getToken(@Body model: AuthorizeModel): Response<TokenModel>

    @Headers("Content-Type: application/json")
    @POST("api/Registration")
    suspend fun register(@Body model: RegisterModel) : Response<Boolean>

    @GET("api/login-is-free/check")
    suspend fun loginIsFree(login:String) : Response<Any>

    @GET("api/user")
    suspend fun getUser(token:String): Any

    //Basket
    @GET("api/basket")
    suspend fun getBasket(@Header("Authorization") token: String) : Response<List<GetBasketResponse>>

}