package com.group4.secondhand.data.model

import com.google.gson.annotations.SerializedName

data class ResponseGetProductSearch(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("image_name")
    val imageName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("Categories")
    val categories: List<Category>
)
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
