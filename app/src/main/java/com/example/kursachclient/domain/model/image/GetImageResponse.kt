package com.example.kursachclient.domain.model.image

import com.google.gson.annotations.SerializedName

data class GetImageResponse (
    @SerializedName("key")
    var id: Int,
    @SerializedName("url")
    var url: String
)