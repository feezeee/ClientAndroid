package com.example.kursachclient.domain

import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService{

    @GET("api/Book")
    suspend fun getBooks(@Query(value="filterName", encoded=true) filterName : String?, @Header("Authorization") token: String): Response<List<GetBookResponse>>

    @GET("api/book/{key}")
    suspend fun getBookByKey(@Path("key") key: Int, @Header("Authorization") token: String): Response<GetBookResponse?>

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

    @POST("api/basket")
    suspend fun addBookToBasket(@Body bookItem: AddBookToBasketRequest, @Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/basket/{key}")
    suspend fun clearBasket(@Path("key") bookId: Int, @Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/basket/clear")
    suspend fun clearBasket(@Header("Authorization") token: String) : Response<Unit>


    //Order
    @GET("api/order")
    suspend fun getOrders(@Header("Authorization") token: String) : Response<List<GetOrderResponse>>

    @PATCH("api/order")
    suspend fun updateStatus(
        @Query(value="orderKey", encoded=true) orderId : Int,
        @Query(value="status", encoded=true) status : String,
        @Header("Authorization") token: String) : Response<Unit>

    @POST("api/order/make-order")
    suspend fun makeOrder(@Header("Authorization") token: String) : Response<Unit>

    //Order-item
    @GET("api/order-item/{key}")
    suspend fun getOrderItems(@Path("key") orderId: Int, @Header("Authorization") token: String) : Response<List<GetOrderItemResponse>>

    @PUT("api/order-item")
    suspend fun updateOrderItem(@Body orderItem: UpdateOrderItemRequest, @Header("Authorization") token: String) : Response<Unit>


}