package com.example.kursachclient.domain.model.authorization

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetTokenRoleModel(
    @SerializedName("access_token")
    val token: String,
    @SerializedName("role")
    val role: String
) : Serializable