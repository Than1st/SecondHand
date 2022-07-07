package com.group4.secondhand.ui.lengkapiinfoakun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetDataUser
import com.group4.secondhand.data.model.ResponseUpdateUser
import com.group4.secondhand.data.model.ResponseUploadProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LengkapiInfoAkunViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private var _responseUpdate = MutableLiveData<Resource<ResponseUpdateUser>>()
    val responseUpdate : LiveData<Resource<ResponseUpdateUser>> get() = _responseUpdate

    private var _getUser = MutableLiveData<Resource<ResponseGetDataUser>>()
    val getUser : LiveData<Resource<ResponseGetDataUser>> get() = _getUser

    fun getUser(token: String){
        viewModelScope.launch {
            _getUser.postValue(Resource.loading())
            try {
                _getUser.postValue(Resource.success(repository.getDataUser(token)))
            } catch (e: Exception){
                _getUser.postValue(Resource.error(e.message?:"Error occured"))
            }
        }
    }

    fun updateDataUser(
        token : String,
        file: File?,
        name: String,
        phoneNumber: String,
        address: String,
        city: String,
    ){
        val requestFile = file?.asRequestBody("image/jpg".toMediaTypeOrNull())
        val image = requestFile?.let { MultipartBody.Part.createFormData("image", file.name, it) }
        val namaRequestBody = name.toRequestBody("text/plain".toMediaType())
        val kotaRequestBody = city.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = address.toRequestBody("text/plain".toMediaType())
        val noHpRequestBody = phoneNumber.toRequestBody("text/plain".toMediaType())
        viewModelScope.launch {
            _responseUpdate.postValue(Resource.loading())
            try {
                _responseUpdate.postValue(Resource.success(repository.updateDataUser(
                    token, image, namaRequestBody, noHpRequestBody,alamatRequestBody, kotaRequestBody
                )))
            } catch (e : Exception){
                _responseUpdate.postValue(Resource.error(e.message?:"Error occured"))
            }

        }
    }
}