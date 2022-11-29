package com.example.kursachclient.domain.model.book

import android.icu.text.CaseMap.Title
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetBookResponse (
    @SerializedName("key")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: ImageModelForGetBookResponse?
) : Serializable