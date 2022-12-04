package com.example.kursachclient.domain.model.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostRegisterModel(
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
) : Serializable