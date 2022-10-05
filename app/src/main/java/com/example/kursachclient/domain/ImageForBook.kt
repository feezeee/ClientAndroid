package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageForBook (
    @SerializedName("url")
    val url: String,
    @SerializedName("is_main")
    val isMain: Boolean
    ) : Serializable