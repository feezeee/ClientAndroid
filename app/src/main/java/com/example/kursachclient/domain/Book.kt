package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    @SerializedName("key")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("discount_percent")
    val discountPercent: Double,
    @SerializedName("count")
    val count: Int,
    @SerializedName("images")
    val images: List<ImageForBook>
): Serializable