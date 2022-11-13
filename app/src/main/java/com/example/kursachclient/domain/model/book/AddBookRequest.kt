package com.example.kursachclient.domain.model.book

import com.google.gson.annotations.SerializedName

data class AddBookRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("price")
    var price : Double,
    @SerializedName("count")
    var count : UInt,
    @SerializedName("image_key")
    var imageId : Int?
)