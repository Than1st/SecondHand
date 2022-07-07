package com.group4.secondhand.ui.editproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.data.model.ResponseDeleteSellerProduct
import com.group4.secondhand.data.model.ResponseUpdateProduk
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
class EditProductViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _token = MutableLiveData<String>()
    val token: MutableLiveData<String> get() = _token

    private var _delete : MutableLiveData<Resource<Response<ResponseDeleteSellerProduct>>> = MutableLiveData()
    val delete : LiveData<Resource<Response<ResponseDeleteSellerProduct>>> get() = _delete

    private val _updateResponse = MutableLiveData<Resource<ResponseUpdateProduk>>()
    val updateResponse : LiveData<Resource<ResponseUpdateProduk>> get() = _updateResponse

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
        id:Int,
        namaProduk: String,
        deskripsiProduk: String,
        hargaProduk: String,
        kategoriProduk: List<Int>,
        alamatPenjual: String,
        image: File?
    ){
        val requestFile = image?.let { reduceFileImage(it).asRequestBody("image/jpg".toMediaTypeOrNull()) }
        val gambarProduk =
            requestFile?.let { MultipartBody.Part.createFormData("image", image.name, it) }
        val namaRequestBody = namaProduk.toRequestBody("text/plain".toMediaType())
        val deskripsiRequestBody = deskripsiProduk.toRequestBody("text/plain".toMediaType())
        val hargaRequestBody = hargaProduk.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = alamatPenjual.toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            _updateResponse.postValue(Resource.loading())
            try {
                _updateResponse.postValue(Resource.success(repository.updateProduct(
                    token,
                    id,
                    gambarProduk,
                    namaRequestBody,
                    deskripsiRequestBody,
                    hargaRequestBody,
                    kategoriProduk,
                    alamatRequestBody
                )))
            } catch (e: Exception){
                _updateResponse.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
    fun deleteSellerProduct(token: String, id:Int){
        viewModelScope.launch {
            _delete.postValue(Resource.loading())
            try {
                _delete.postValue(Resource.success(repository.deleteSellerProduct(token,id)))
            } catch (e: Exception) {
                _delete.postValue((Resource.error(e.localizedMessage ?: "Error occured")))
            }
        }
    }

    fun getToken() {
        viewModelScope.launch {
            repository.getToken().collect {
                _token.postValue(it)
            }
        }
    }
}