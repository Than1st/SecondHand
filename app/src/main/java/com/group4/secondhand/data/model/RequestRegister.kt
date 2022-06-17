package com.group4.secondhand.data.model

import com.google.gson.annotations.SerializedName

data class RequestRegister(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String? = "",
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("image")
    val imageUrl: Any? = null
)
