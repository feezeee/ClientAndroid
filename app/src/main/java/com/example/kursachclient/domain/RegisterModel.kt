package com.example.kursachclient.domain

import com.google.gson.annotations.SerializedName

data class RegisterModel(
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("phone_number")
    var phoneNumber: String,
    @SerializedName("login")
    var login: String,
    @SerializedName("password")
    var password: String
)