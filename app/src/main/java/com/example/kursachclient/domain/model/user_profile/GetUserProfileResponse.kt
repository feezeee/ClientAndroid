package com.example.kursachclient.domain.model.user_profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetUserProfileResponse (
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName : String,
    @SerializedName("phone_number")
    var phoneNumber : String,
    @SerializedName("registration_date")
    var registration_date : Long
) : Serializable