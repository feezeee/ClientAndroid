package com.example.kursachclient.domain.model.login_is_free

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetLoginStatus (
    @SerializedName("status")
    var isFree: Boolean) : Serializable