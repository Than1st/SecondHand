package com.group4.secondhand.data.api

class ApiHelper(private val apiService: ApiService) {
//    contoh api service
//    suspend fun getSellerProduct(apiKey: String) = apiService.getSellerProduct(apiKey)

    suspend fun getCategoryHome() = apiService.getCategoryHome()
    suspend fun getProduct(status:String,categoryId:String) = apiService.getProduct(status,categoryId)
}