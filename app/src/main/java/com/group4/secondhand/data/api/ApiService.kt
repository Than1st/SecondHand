package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // SELLER
    @GET("seller/category")
    suspend fun getCategoryHome(): List<ResponseCategoryHome>

    @GET("seller/product")
    suspend fun getSellerProduct(@Header("access_token") token: String): List<ResponseSellerProduct>

    @DELETE("seller/product/{id}")
    suspend fun deleteSellerProduct(
        @Header("access_token") token: String,
        @Path("id") id: Int
    ): Response<ResponseDeleteSellerProduct>

    @GET("seller/order")
    suspend fun getSellerOrder(@Header("access_token") token: String): List<ResponseSellerOrder>

    @PATCH("seller/order/{id}")
    suspend fun approveOrder(
        @Header("access_token") token: String,
        @Path("id") orderId: Int,
        @Body requestApproveOrder: RequestApproveOrder
    ): ResponseApproveOrder


    @Multipart
    @POST("seller/product")
    suspend fun uploadProduct(
        @Header("access_token") token: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("base_price") base_price: RequestBody?,
        @Part("category_ids") categoryIds: List<Int>,
        @Part("location") location: RequestBody?,
    ): ResponseUploadProduct

    @Multipart
    @PUT("seller/product/{id}")
    suspend fun updateProduct(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("base_price") base_price: RequestBody?,
        @Part("category_ids") categoryIds: List<Int>,
        @Part("location") location: RequestBody?,
    ): ResponseUpdateProduk

    // BUYER
    @GET("buyer/product")
    suspend fun getProduct(
        @Query("status") status: String,
        @Query("category_id") categoryId: String
    ): List<ResponseGetProduct>

    @GET("buyer/product/{id}")
    suspend fun getProdukById(
        @Path("id") id: Int
    ): Response<ResponseBuyerProductById>

    @POST("buyer/order")
    suspend fun buyerOrder(
        @Header("access_token") token: String,
        @Body requestBuyerOrder: RequestBuyerOrder
    ): Response<ResponseBuyerOrder>

    @GET("buyer/order")
    suspend fun getBuyerOrder(
        @Header("access_token") token: String
    ): List<ResponseGetBuyerOrder>

    // AUTH
    @POST("auth/register")
    suspend fun authRegister(@Body requestRegister: RequestRegister): ResponseRegister

    @POST("auth/login")
    suspend fun authLogin(@Body requestLogin: RequestLogin): ResponseLogin

    @GET("auth/user")
    suspend fun getDataUser(@Header("access_token") token: String): ResponseGetDataUser

    @Multipart
    @PUT("auth/change-password")
    suspend fun changePassword(
        @Header("access_token") token: String,
        @Part("current_password") currentPassword: RequestBody,
        @Part("new_password") newPassword: RequestBody,
        @Part("confirm_password") confirmPassword: RequestBody
    ) : Response<ResponseChangePassword>

    @Multipart
    @PUT("auth/user")
    suspend fun updateDataUser(
        @Header("access_token") token: String,
        @Part file: MultipartBody.Part? = null,
        @Part("full_name") name: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("email") email: RequestBody? = null,
        @Part("password") password: RequestBody? = null
    ): ResponseUpdateUser

    // NOTIFICATION
    @GET("notification")
    suspend fun getNofitication(@Header("access_token") token: String): List<ResponseNotification>

    @GET("notification/{id}")
    suspend fun getNofiticationById(
        @Header("access_token") token: String,
        @Path("id") id: String
    ): List<ResponseNotification>

    @PATCH("notification/{id}")
    suspend fun markReadNotification(@Path("id") id: Int)

}