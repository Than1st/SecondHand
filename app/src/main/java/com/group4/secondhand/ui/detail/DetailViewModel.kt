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


}