package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponsePostWishlist(
    @SerializedName("name")
    val name: String,
    @SerializedName("product")
    val product: ProductWishlist
)
