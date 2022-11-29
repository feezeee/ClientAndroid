package com.example.kursachclient.domain.model.order_item

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateOrderItemRequest(
    @SerializedName("key")
    var id: Int,
    @SerializedName("count")
    var count: UInt
) : Serializable