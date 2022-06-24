package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiHelper(private val apiService: ApiService) {
    // SELLER
    suspend fun getCategoryHome() = apiService.getCategoryHome()
    suspend fun getSellerProduct(token: String) = apiService.getSellerProduct(token)
    suspend fun getSellerOrder(token: String) = apiService.getSellerOrder(token)

    suspend fun uploadProduct(
        token: String,
        file : MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiService.uploadProduct(token, file, name, description, base_price, categoryIds, location)

    // BUYER
    suspend fun getProduct(status: String, categoryId: String) =
        apiService.getProduct(status, categoryId)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) =
        apiService.authRegister(requestRegister)

    suspend fun authLogin(requestLogin: RequestLogin) = apiService.authLogin(requestLogin)
    suspend fun getDataUser(token: String) = apiService.getDataUser(token)
    suspend fun updateDataUser(
        token : String,
        image: MultipartBody.Part?,
        name: RequestBody?,
        phoneNumber: RequestBody?,
        address: RequestBody?,
        city: RequestBody?,
    ) = apiService.updateDataUser(token, image, name, phoneNumber, address, city)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiService.getNofitication(token)
}