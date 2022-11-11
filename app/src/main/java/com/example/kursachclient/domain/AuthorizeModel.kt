package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName

data class AuthorizeModel (
    @SerializedName("login")
    var login: String,
    @SerializedName("password")
    var password: String
)