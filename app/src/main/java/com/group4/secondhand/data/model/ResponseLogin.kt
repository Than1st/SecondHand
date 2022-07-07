package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int
)