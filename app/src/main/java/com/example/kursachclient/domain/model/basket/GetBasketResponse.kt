package com.example.kursachclient.domain.model.basket

import com.example.kursachclient.domain.model.book.ImageModelForGetBookResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetBasketResponse(

    @SerializedName("book")
    val book: BookModelForGetBasketResponse,
    @SerializedName("count")
    val count: Int
) : Serializable
