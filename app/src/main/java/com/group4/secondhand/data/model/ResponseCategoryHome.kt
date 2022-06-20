package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

data class ResponseCategoryHome(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)