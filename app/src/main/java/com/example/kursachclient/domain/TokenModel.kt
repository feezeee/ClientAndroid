package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("token")
    val token: String
)