package com.group4.secondhand.data.model


import com.google.gson.annotations.SerializedName
import com.group4.secondhand.data.model.pagingProduk.Product

data class ResponseGetProduct(
    @SerializedName("data")
    val `data`: List<Product>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val nextPage:Int? = null
)