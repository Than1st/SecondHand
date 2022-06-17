package com.group4.secondhand.data.api

import com.group4.secondhand.data.model.*

class ApiHelper(private val apiService: ApiService) {
    // SELLER
    suspend fun getCategoryHome() = apiService.getCategoryHome()

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) = apiService.authRegister(requestRegister)
    suspend fun authLogin(requestLogin: RequestLogin) = apiService.authLogin(requestLogin)
    suspend fun getDataUser(token : String) = apiService.getDataUser(token)
    suspend fun updateDataUser(token : String) = apiService.updateDataUser(token)
}