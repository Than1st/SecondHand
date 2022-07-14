package com.group4.secondhand.data.model

import com.google.gson.annotations.SerializedName

data class RequestUpdateStatusProduk (
    @SerializedName("status")
    val status: String
)