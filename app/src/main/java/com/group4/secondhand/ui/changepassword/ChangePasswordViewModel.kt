package com.group4.secondhand.ui.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseChangePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private var _response : MutableLiveData<Resource<Response<ResponseChangePassword>>> = MutableLiveData()
    val response : LiveData<Resource<Response<ResponseChangePassword>>> get() = _response

    fun changePassword(token: String, currentPassword: String, newPassword: String, confPassword: String){
        val currentRequestBody = currentPassword.toRequestBody("text/plain".toMediaType())
        val newRequestBody = newPassword.toRequestBody("text/plain".toMediaType())
        val confRequestBody = confPassword.toRequestBody("text/plain".toMediaType())
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            try {
                _response.postValue(Resource.success(repository.changePassword(token, currentRequestBody, newRequestBody, confRequestBody)))
            } catch (e: Exception){
                _response.postValue(Resource.error(e.localizedMessage?:"Error occurred"))
            }
        }
    }
}