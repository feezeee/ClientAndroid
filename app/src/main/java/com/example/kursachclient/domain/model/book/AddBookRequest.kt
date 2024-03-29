package com.example.kursachclient.domain.model.book

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddBookRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("price")
    var price : Double,
//    @SerializedName("image_key")
//    var imageId : Int?
    @SerializedName("image")
    var image : String?
) : Serializable