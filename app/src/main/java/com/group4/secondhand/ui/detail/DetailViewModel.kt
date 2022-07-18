package com.group4.secondhand.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    private var _token = MutableLiveData<Resource<String>>()
    val token : LiveData<Resource<String>> get() = _token

    private var _buyerOrder = MutableLiveData<Resource<Response<ResponseBuyerOrder>>>()
    val buyerOrder : LiveData<Resource<Response<ResponseBuyerOrder>>> get()= _buyerOrder

    private val _detailProduk : MutableLiveData<Resource<Response<ResponseBuyerProductById>>> = MutableLiveData()
    val detailProduk : LiveData<Resource<Response<ResponseBuyerProductById>>> get() = _detailProduk

    private val _getBuyerOrder :  MutableLiveData<Resource<List<ResponseGetBuyerOrder>>> = MutableLiveData()
    val getBuyerOrder : LiveData<Resource<List<ResponseGetBuyerOrder>>> get() = _getBuyerOrder

    private val _getBuyerWishlist : MutableLiveData<Resource<List<ResponseGetBuyerWishlist>>> = MutableLiveData()
    val getBuyerWishlist : LiveData<Resource<List<ResponseGetBuyerWishlist>>> get() = _getBuyerWishlist

    private val _addWishlist : MutableLiveData<Resource<Response<ResponsePostWishlist>>> = MutableLiveData()
    val addWishlist : LiveData<Resource<Response<ResponsePostWishlist>>> get() = _addWishlist

    private val _removeWishlist : MutableLiveData<Resource<Response<ResponseRemoveWishlist>>> = MutableLiveData()
    val removeWishlist : LiveData<Resource<Response<ResponseRemoveWishlist>>> get() = _removeWishlist

    fun getBuyerOrder (token: String){
        viewModelScope.launch {
            _getBuyerOrder.postValue(Resource.loading())
            try{
                _getBuyerOrder.postValue(Resource.success(repository.getBuyerOrder(token)))
            } catch (e:Exception){
                _getBuyerOrder.postValue(Resource.error(e.localizedMessage?:"Error occurred"))
            }
        }
    }

    fun getToken(){
        viewModelScope.launch {
            _token.postValue(Resource.loading())
            try {
                repository.getToken().collect{
                    _token.postValue(Resource.success(it))
                }
            } catch (e: Exception){
                _token.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun buyerOrder(token:String,requestBuyerOrder: RequestBuyerOrder){
        viewModelScope.launch {
            _buyerOrder.postValue(Resource.loading())
            try{
                _buyerOrder.postValue(Resource.success(repository.buyerOrder(token, requestBuyerOrder)))
            } catch (e:Exception){
                _buyerOrder.postValue(Resource.error(e.localizedMessage?:"Error occurred"))
            }
        }
    }


    fun getProdukById(id: Int) {
        viewModelScope.launch {
            _detailProduk.postValue(Resource.loading())
            try {
                _detailProduk.postValue(Resource.success(repository.getProductById(id)))
            } catch (exception: Exception) {
                _detailProduk.postValue(
                    Resource.error(
                        exception.localizedMessage ?: "Error occured"
                    )
                )
            }
        }
    }

    fun getBuyerWishlist(token: String) {
        viewModelScope.launch {
            _getBuyerWishlist.postValue(Resource.loading())
            try {
                _getBuyerWishlist.postValue(Resource.success(repository.getBuyerWishlist(token)))
            } catch (e: Exception) {
                _getBuyerWishlist.postValue(Resource.error(e.localizedMessage ?: "Error occurred"))
            }
        }
    }

    fun addWishlist(token: String,product_id:Int){
        val id = product_id.toString().toRequestBody("text/plain".toMediaType())
         viewModelScope.launch {
             _addWishlist.postValue(Resource.loading())
             try {
                 _addWishlist.postValue(Resource.success(repository.addWishlist(token,id)))
             }catch (e: Exception){
                 _addWishlist.postValue(Resource.error(e.localizedMessage?: "Error occured"))
             }
         }
    }

    fun removeWishlist(token: String,id: Int){
        viewModelScope.launch {
            _removeWishlist.postValue(Resource.loading())
            try {
                _removeWishlist.postValue(Resource.success(repository.removeWishlist(token,id)))
            }catch (e: Exception){
                _removeWishlist.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}