package com.example.kursachclient.domain.model.book

import com.google.gson.annotations.SerializedName

data class UpdateBookRequest(
    @SerializedName("key")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("price")
    var price : String,
    @SerializedName("count")
    var count : UInt,
    @SerializedName("image_key")
    var imageId : Int?
)
