package com.example.kursachclient.domain.model.authorization

import com.google.gson.annotations.SerializedName

data class PostAuthorizerModel (
    @SerializedName("login")
    var login: String,
    @SerializedName("password")
    var password: String
)