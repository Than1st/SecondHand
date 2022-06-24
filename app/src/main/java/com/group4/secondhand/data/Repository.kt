package com.group4.secondhand.data

import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(private val apiHelper: ApiHelper, private val userPreferences: UserPreferences) {
    // SELLER
    suspend fun getCategoryHome() = apiHelper.getCategoryHome()
    suspend fun getSellerProduct(token: String) = apiHelper.getSellerProduct(token)
    suspend fun getSellerOrder(token: String) = apiHelper.getSellerOrder(token)
    suspend fun uploadProduct(
        token: String,
        file : MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiHelper.uploadProduct(token, file, name, description, base_price, categoryIds, location)

    // BUYER
    suspend fun getProduct(status: String, categoryId: String) =
        apiHelper.getProduct(status, categoryId)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) =
        apiHelper.authRegister(requestRegister)

    suspend fun authLogin(requestLogin: RequestLogin) = apiHelper.authLogin(requestLogin)
    suspend fun getDataUser(token: String) = apiHelper.getDataUser(token)
    suspend fun updateDataUser(token: String, image: MultipartBody.Part?, name: RequestBody?) =
        apiHelper.updateDataUser(token, image, name)
    suspend fun getDataUser(token : String) = apiHelper.getDataUser(token)
    suspend fun updateDataUser(
        token : String,
        image: MultipartBody.Part?,
        name: RequestBody?,
        phoneNumber: RequestBody?,
        address: RequestBody?,
        city: RequestBody?,
    ) = apiHelper.updateDataUser(token, image, name, phoneNumber, address, city)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiHelper.getNotification(token)

    // DATA STORE
    suspend fun setToken(token: String) = userPreferences.setToken(token)
    fun getToken() = userPreferences.getToken()
    suspend fun deleteToken() = userPreferences.deleteToken()

}