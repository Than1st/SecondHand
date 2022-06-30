package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponseDeleteSellerProduct(
    @SerializedName("name")
    val name: String,
    @SerializedName("msg")
    val msg: String
)