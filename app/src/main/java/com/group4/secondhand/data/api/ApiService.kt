package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
//    contoh header pakai access_code / API Key
//    @GET("seller/product")
//    suspend fun getSellerProduct(
//        @Header ("access_token") apiKey : String
//    ) : Response<GetSellerProductResponse>

    @GET("seller/category")
    suspend fun getCategoryHome() : List<ResponseCategoryHome>

    @GET("seller/product")
    suspend fun getSellerProduct(@Header("access_token") token: String) : List<ResponseSellerProduct>


    // BUYER
    @GET("buyer/product")
    suspend fun getProduct(
        @Query("status") status : String,
        @Query("category_id") categoryId : String
    ) : List<ResponseGetProduct>

    // AUTH
    @POST("auth/register")
    suspend fun authRegister(@Body requestRegister: RequestRegister) : ResponseRegister

    @POST("auth/login")
    suspend fun authLogin(@Body requestLogin: RequestLogin) : ResponseLogin

    @GET("auth/user")
    suspend fun getDataUser(@Header("access_token") token : String) : ResponseGetDataUser

    @Multipart
    @PUT("auth/user")
    suspend fun updateDataUser(
        @Header("access_token") token : String,
        @Part file: MultipartBody.Part?,
        @Part("full_name") name : RequestBody?
    ) : ResponseUpdateUser

    // NOTIFICATION
    @GET("notification")
    suspend fun getNofitication(@Header("access_token") token: String) : List<ResponseNotification>

    @GET("notification/{id}")
    suspend fun getNofiticationById(
        @Header("access_token") token: String,
        @Path("id") id: String
    ) : List<ResponseNotification>

    @PATCH("notification/{id}")
    suspend fun markReadNotification(@Path("id") id: Int)

}