package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import com.group4.secondhand.data.model.RequestUpdateUser
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiHelper(private val apiService: ApiService) {
    // SELLER
    suspend fun getCategoryHome() = apiService.getCategoryHome()

    // BUYER
    suspend fun getProduct(status:String,categoryId:String) = apiService.getProduct(status,categoryId)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) = apiService.authRegister(requestRegister)
    suspend fun authLogin(requestLogin: RequestLogin) = apiService.authLogin(requestLogin)
    suspend fun getDataUser(token : String) = apiService.getDataUser(token)
    suspend fun updateDataUser(token : String, image: MultipartBody.Part?, name: RequestBody?) = apiService.updateDataUser(token, image, name)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiService.getNofitication(token)
}