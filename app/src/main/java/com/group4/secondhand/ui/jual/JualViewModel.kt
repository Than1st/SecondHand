package com.group4.secondhand.ui.jual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.data.model.ResponseGetDataUser
import com.group4.secondhand.data.model.ResponseUploadProduct
import com.group4.secondhand.ui.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JualViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _uploadResponse = MutableLiveData<Resource<Response<ResponseUploadProduct>>>()
    val uploadResponse : LiveData<Resource<Response<ResponseUploadProduct>>> get() = _uploadResponse

    private var _alreadyLogin = MutableLiveData<String>()
    val alreadyLogin : LiveData<String> get() = _alreadyLogin

    private var _user = MutableLiveData<Resource<ResponseGetDataUser>>()
    val user : LiveData<Resource<ResponseGetDataUser>> get() = _user

    private var _categoryList = MutableLiveData<List<String>>()
    val categoryList : LiveData<List<String>> get() = _categoryList

    private val _category = MutableLiveData<Resource<List<ResponseCategoryHome>>>()
    val category : LiveData<Resource<List<ResponseCategoryHome>>> get() = _category

    fun getCategoryHome(){
        viewModelScope.launch {
            _category.postValue(Resource.loading())
            try {
                _category.postValue(Resource.success(repository.getCategoryHome()))
            }catch (e:Exception){
                _category.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun addCategory(category: List<String>){
        _categoryList.postValue(category)
    }

    fun uploadProduk(
        token: String,
        namaProduk: String,
        deskripsiProduk: String,
        hargaProduk: String,
        kategoriProduk: List<Int>,
        alamatPenjual: String,
        image: File
    ){
        val requestFile = reduceFileImage(image).asRequestBody("image/jpg".toMediaTypeOrNull())
        val gambarProduk = MultipartBody.Part.createFormData("image", image.name, requestFile)
        val namaRequestBody = namaProduk.toRequestBody("text/plain".toMediaType())
        val deskripsiRequestBody = deskripsiProduk.toRequestBody("text/plain".toMediaType())
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
                    kategoriProduk,
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