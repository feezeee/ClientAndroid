package com.example.kursachclient.domain.model.order

import com.google.gson.annotations.SerializedName

data class PayOrderRequest (
    @SerializedName("key")
    var id: Int,
    @SerializedName("price")
    var price: Double
)