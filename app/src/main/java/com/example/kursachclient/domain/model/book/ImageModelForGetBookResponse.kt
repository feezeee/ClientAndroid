package com.example.kursachclient.domain.model.book

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageModelForGetBookResponse (
    @SerializedName("key")
    val id: Int,
    @SerializedName("url")
    val url: String
) : Serializable