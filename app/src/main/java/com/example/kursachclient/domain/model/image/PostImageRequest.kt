package com.example.kursachclient.domain.model.image

import com.google.gson.annotations.SerializedName

data class PostImageRequest(
    @SerializedName("image")
    var image: String,)
