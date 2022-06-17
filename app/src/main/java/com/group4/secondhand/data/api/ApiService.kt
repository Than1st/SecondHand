package com.group4.secondhand.data.api

import com.group4.secondhand.data.api.model.ResponseCategoryHome
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
//    contoh header pakai access_code / API Key
//    @GET("seller/product")
//    suspend fun getSellerProduct(
//        @Header ("access_token") apiKey : String
//    ) : Response<GetSellerProductResponse>

    @GET("seller/category")
    suspend fun getCategoryHome() : List<ResponseCategoryHome>
}