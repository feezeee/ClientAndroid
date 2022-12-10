package com.example.kursachclient.domain

import com.example.kursachclient.domain.model.authorization.PostAuthorizerModel
import com.example.kursachclient.domain.model.authorization.GetTokenRoleModel
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import com.example.kursachclient.domain.model.login_is_free.GetLoginStatus
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.order.PayOrderRequest
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import com.example.kursachclient.domain.model.registration.PostRegisterModel
import com.example.kursachclient.domain.model.user_profile.GetUserProfileResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService{

    @GET("api/book")
    suspend fun getBooks(@Query(value="filterName", encoded=true) filterName : String?, @Header("Authorization") token: String): Response<MutableList<GetBookResponse>>

    @GET("api/book/{key}")
    suspend fun getBookByKey(@Path("key") key: Int, @Header("Authorization") token: String): Response<GetBookResponse?>

    @POST("api/book")
    suspend fun addBook(@Body book: AddBookRequest, @Header("Authorization") token: String): Response<Unit>

    @PUT("api/book")
    suspend fun updateBook(@Body book: UpdateBookRequest, @Header("Authorization") token: String): Response<Unit>

    @DELETE("api/book/{key}")
    suspend fun deleteBook(@Path("key") id: Int, @Header("Authorization") token: String): Response<Unit>



    @POST("api/authorization")
    suspend fun getToken(@Body model: PostAuthorizerModel): Response<GetTokenRoleModel>

    @Headers("Content-Type: application/json")
    @POST("api/Registration")
    suspend fun register(@Body model: PostRegisterModel) : Response<Unit>

    @GET("api/login-is-free/check")
    suspend fun loginIsFree(@Query(value = "login", encoded = true) login:String) : Response<GetLoginStatus>

    //Basket
    @GET("api/basket")
    suspend fun getBasket(@Header("Authorization") token: String) : Response<MutableList<GetBasketResponse>>

    @POST("api/basket")
    suspend fun addBookToBasket(@Body bookItem: AddBookToBasketRequest, @Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/basket/{key}")
    suspend fun clearBasket(@Path("key") bookId: Int, @Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/basket/clear")
    suspend fun clearBasket(@Header("Authorization") token: String) : Response<Unit>

    //UserProfile
    @GET("api/user/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String) : Response<GetUserProfileResponse>

    //Order
    @GET("api/order")
    suspend fun getOrders(@Query(value="filterName", encoded=true) filterName : String?, @Header("Authorization") token: String) : Response<MutableList<GetOrderResponse>>

    @GET("api/order/personal")
    suspend fun getPersonalOrders(@Header("Authorization") token: String) : Response<MutableList<GetOrderResponse>>

    @GET("api/order/{key}")
    suspend fun getOrderById(@Path("key") orderId: Int, @Header("Authorization") token: String) : Response<GetOrderResponse>

    @PATCH("api/order")
    suspend fun updateStatus(
        @Query(value="orderKey", encoded=true) orderId : Int,
        @Query(value="status", encoded=true) status : String,
        @Header("Authorization") token: String) : Response<Unit>

    @POST("api/order/make-order")
    suspend fun makeOrder(@Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/order/{key}")
    suspend fun deleteOrder(@Path("key") orderId: Int, @Header("Authorization") token: String) : Response<Unit>

    @POST("api/order/pay-order")
    suspend fun payOrder(@Body payOrder: PayOrderRequest, @Header("Authorization") token: String) : Response<Unit>

    //Order-item
    @GET("api/order-item/{key}")
    suspend fun getOrderItems(@Path("key") orderId: Int, @Header("Authorization") token: String) : Response<MutableList<GetOrderItemResponse>>

    @PUT("api/order-item")
    suspend fun updateOrderItem(@Body orderItem: UpdateOrderItemRequest, @Header("Authorization") token: String) : Response<Unit>

    @DELETE("api/order-item/{key}")
    suspend fun deleteOrderItem(@Path("key") itemId: Int, @Header("Authorization") token: String) : Response<Unit>


}