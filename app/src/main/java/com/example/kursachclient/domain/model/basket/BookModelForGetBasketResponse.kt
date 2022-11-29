package com.example.kursachclient.domain.model.basket

import com.example.kursachclient.domain.model.book.ImageModelForGetBookResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookModelForGetBasketResponse(
    @SerializedName("key")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: ImageModelForBookModel?
): Serializable
