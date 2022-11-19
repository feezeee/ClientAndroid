package com.example.kursachclient.domain.model.basket

import com.google.gson.annotations.SerializedName

data class ImageModelForBookModel(
    @SerializedName("key")
    val id: Int,
    @SerializedName("url")
    val url: String
)
