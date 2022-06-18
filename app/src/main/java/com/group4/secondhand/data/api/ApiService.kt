package com.group4.secondhand.data.api

import com.group4.secondhand.data.api.model.ResponseCategoryHome
import com.group4.secondhand.data.api.model.ResponseGetProduct
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    contoh header pakai access_code / API Key
//    @GET("seller/product")
//    suspend fun getSellerProduct(
//        @Header ("access_token") apiKey : String
//    ) : Response<GetSellerProductResponse>

    @GET("seller/category")
    suspend fun getCategoryHome() : List<ResponseCategoryHome>

    // BUYER
    @GET("buyer/product")
    suspend fun getProduct(
        @Query("status") status : String,
        @Query("category_id") categoryId : String
    ) : List<ResponseGetProduct>
}