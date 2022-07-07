package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiHelper(private val apiService: ApiService) {
    // SELLER
    suspend fun getCategoryHome() = apiService.getCategoryHome()
    suspend fun getSellerProduct(token: String) = apiService.getSellerProduct(token)
    suspend fun getSellerOrder(token: String) = apiService.getSellerOrder(token)
    suspend fun deleteSellerProduct(token: String, id: Int) = apiService.deleteSellerProduct(token,id)
    suspend fun approveOrder(token: String, id: Int, requestApproveOrder: RequestApproveOrder) = apiService.approveOrder(token, id, requestApproveOrder)

    suspend fun uploadProduct(
        token: String,
        file: MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiService.uploadProduct(token, file, name, description, base_price, categoryIds, location)

    suspend fun updateProduct(
        token: String,
        id: Int,
        file: MultipartBody.Part?,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiService.updateProduct(token,id, file, name, description, base_price, categoryIds, location)

    // BUYER
    suspend fun getProduct(status: String, categoryId: String) =
        apiService.getProduct(status, categoryId)

    suspend fun getProductById ( id : Int ) = apiService.getProdukById(id)

    suspend fun buyerOrder(token: String, requestBuyerOrder: RequestBuyerOrder) =
        apiService.buyerOrder(token, requestBuyerOrder)
    suspend fun getBuyerOrder(token: String) =
        apiService.getBuyerOrder(token)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) =
        apiService.authRegister(requestRegister)

    suspend fun authLogin(requestLogin: RequestLogin) = apiService.authLogin(requestLogin)
    suspend fun getDataUser(token: String) = apiService.getDataUser(token)
    suspend fun changePassword(token: String, currentPassword: RequestBody, newPassword: RequestBody, confPassword: RequestBody) = apiService.changePassword(token, currentPassword, newPassword, confPassword)
    suspend fun updateDataUser(
        token: String,
        image: MultipartBody.Part?,
        name: RequestBody?,
        phoneNumber: RequestBody?,
        address: RequestBody?,
        city: RequestBody?,
    ) = apiService.updateDataUser(token, image, name, phoneNumber, address, city)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiService.getNofitication(token)
}