package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("count")
    val count: Int
): Serializable