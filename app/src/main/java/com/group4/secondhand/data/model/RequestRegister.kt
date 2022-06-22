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
    val phoneNumber: String? = "no_number",
    @SerializedName("address")
    val address: String? = "no_address",
    @SerializedName("image")
    val imageUrl: Any? = "no_image",
    @SerializedName("city")
    val city: String? = "no_city"

)
