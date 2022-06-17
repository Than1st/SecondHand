package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.*
import retrofit2.http.*

interface ApiService {
//    contoh header pakai access_code / API Key
//    @GET("seller/product")
//    suspend fun getSellerProduct(
//        @Header ("access_token") apiKey : String
//    ) : Response<GetSellerProductResponse>

    @GET("seller/category")
    suspend fun getCategoryHome() : List<ResponseCategoryHome>

    // AUTH
    @POST("auth/register")
    suspend fun authRegister(@Body requestRegister: RequestRegister) : ResponseRegister

    @POST("auth/login")
    suspend fun authLogin(@Body requestLogin: RequestLogin) : ResponseLogin

    @GET("auth/user/{id}")
    suspend fun getDataUser(@Header("access_token") token : String) : ResponseGetDataUser

    @PUT("auth/user/{id}")
    suspend fun updateDataUser(@Header("access_token") token : String) : ResponseUpdateUser
}