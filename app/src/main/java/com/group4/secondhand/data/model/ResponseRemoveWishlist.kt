package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponseRemoveWishlist(
    @SerializedName("name")
    val name: String,
    @SerializedName("message")
    val message: String
)