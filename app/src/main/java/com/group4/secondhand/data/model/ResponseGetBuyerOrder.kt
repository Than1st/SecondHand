package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName

    data class ResponseGetBuyerOrder(
        @SerializedName("base_price")
        val basePrice: Int,
        @SerializedName("buyer_id")
        val buyerId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_url")
        val imageUrl: Any,
        @SerializedName("price")
        val price: Int,
        @SerializedName("transaction_date")
        val transaction_date: String,
        @SerializedName("Product")
        val product: Product,
        @SerializedName("product_id")
        val productId: Int,
        @SerializedName("product_name")
        val productName: Any,
        @SerializedName("status")
        val status: String,
        @SerializedName("User")
        val user: User
    ) {
        data class Product(
            @SerializedName("base_price")
            val basePrice: Int,
            @SerializedName("description")
            val description: String,
            @SerializedName("image_name")
            val imageName: String,
            @SerializedName("image_url")
            val imageUrl: String,
            @SerializedName("location")
            val location: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("status")
            val status: String,
            @SerializedName("User")
            val user: UserOwnerOrder,
            @SerializedName("user_id")
            val userId: Int
        )

        data class UserOwnerOrder(
            @SerializedName("address")
            val address: String,
            @SerializedName("city")
            val city: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("full_name")
            val fullName: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("phone_number")
            val phoneNumber: String
        )

        data class User(
            @SerializedName("address")
            val address: String,
            @SerializedName("city")
            val city: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("full_name")
            val fullName: String,
            @SerializedName("phone_number")
            val phoneNumber: String
        )
    }