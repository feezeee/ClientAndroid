package com.example.kursachclient.domain.model.basket

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddOrRemoveBookFromBasketRequest(
    @SerializedName("book_key")
    var bookId: Int,
    @SerializedName("count")
    var count: UInt
) : Serializable