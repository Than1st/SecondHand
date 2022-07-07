package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class RequestBuyerOrder(
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("bid_price")
    val bidPrice: Int
)