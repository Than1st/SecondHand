package com.group4.secondhand.data

import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.datastore.UserPreferences

class Repository(private val apiHelper: ApiHelper, private val userPreferences: UserPreferences) {
    //User Pref
//    suspend fun setOnboarding(status: Boolean){
//        userPreferences.onBoardingPref(status)
//    }
}