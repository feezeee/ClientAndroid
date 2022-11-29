package com.example.kursachclient.domain.model.order

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Date
import java.time.LocalDateTime

data class GetOrderResponse(
    @SerializedName("key")
    var id: Int,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName : String,
    @SerializedName("phone_number")
    var phoneNumber : String,
    @SerializedName("created_date")
    var createdDate : Long,
    @SerializedName("full_price")
    var fullPrice : Double,
    @SerializedName("status")
    var status: String
): Serializable
