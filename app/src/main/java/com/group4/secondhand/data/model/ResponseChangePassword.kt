package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponseChangePassword(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)