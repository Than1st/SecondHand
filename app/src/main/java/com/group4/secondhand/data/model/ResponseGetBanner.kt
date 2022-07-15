package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName


data class ResponseGetBanner(
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("updatedAt")
        val updatedAt: String
    )
