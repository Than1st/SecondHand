package com.group4.secondhand.ui.jual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetDataUser
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
class JualViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _uploadResponse = MutableLiveData<Resource<ResponseUploadProduct>>()
    val uploadResponse : LiveData<Resource<ResponseUploadProduct>> get() = _uploadResponse

    private var _alreadyLogin = MutableLiveData<String>()
    val alreadyLogin : LiveData<String> get() = _alreadyLogin

    private var _user = MutableLiveData<Resource<ResponseGetDataUser>>()
    val user : LiveData<Resource<ResponseGetDataUser>> get() = _user

    fun uploadProduk(
        token: String,
        namaProduk: String,
        deskripsiProduk: String,
        hargaProduk: String,
        kategoriProduk: Int,
        alamatPenjual: String,
        image: File
    ){
        val kategoriList: MutableList<Int> = ArrayList()
        kategoriList.add(kategoriProduk)
        val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
        val gambarProduk = MultipartBody.Part.createFormData("image", image.name, requestFile)
        val namaRequestBody = namaProduk.toRequestBody("text/plain".toMediaType())
        val deskripsiRequestBody = deskripsiProduk.toRequestBody("text/plain".toMediaType())
//        val kategoriRequestBody = kategoriProduk.toString().toRequestBody("text/plain".toMediaType())
        val hargaRequestBody = hargaProduk.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = alamatPenjual.toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            _uploadResponse.postValue(Resource.loading())
            try {
                _uploadResponse.postValue(Resource.success(repository.uploadProduct(
                    token,
                    gambarProduk,
                    namaRequestBody,
                    deskripsiRequestBody,
                    hargaRequestBody,
                    kategoriList,
                    alamatRequestBody
                )))
            } catch (e: Exception){
                _uploadResponse.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun getToken(){
        viewModelScope.launch {
            repository.getToken().collect{
                _alreadyLogin.postValue(it)
            }
        }
    }

    fun getUserData(token: String){
        viewModelScope.launch {
            _user.postValue(Resource.loading())
            try {
                _user.postValue(Resource.success(repository.getDataUser(token)))
            } catch (e: Exception){
                _user.postValue(Resource.error(e.localizedMessage?: "Error Occured"))
            }
        }
    }

//    fun uploadProduct(
//
//    ){
//
//    }
}