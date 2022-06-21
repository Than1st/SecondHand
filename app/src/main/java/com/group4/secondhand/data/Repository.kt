package com.group4.secondhand.data

import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import com.group4.secondhand.data.model.User

class Repository(private val apiHelper: ApiHelper, private val userPreferences: UserPreferences) {
    // SELLER
    suspend fun getCategoryHome() = apiHelper.getCategoryHome()

    // BUYER
    suspend fun getProduct(status:String,categoryId:String) = apiHelper.getProduct(status,categoryId)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) = apiHelper.authRegister(requestRegister)
    suspend fun authLogin(requestLogin: RequestLogin) = apiHelper.authLogin(requestLogin)
    suspend fun getDataUser(token : String) = apiHelper.getDataUser(token)
    suspend fun updateDataUser(token : String) = apiHelper.updateDataUser(token)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiHelper.getNotification(token)

    // DATA STORE
    suspend fun setToken(token: String) = userPreferences.setToken(token)
    fun getToken() = userPreferences.getToken()
    suspend fun deleteToken() = userPreferences.deleteToken()

}