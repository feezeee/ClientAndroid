package com.example.kursachclient.domain.model.order_item

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetOrderItemResponse(
    @SerializedName("key")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("price")
    var price: Double,
    @SerializedName("count")
    var count: UInt
) : Serializable
