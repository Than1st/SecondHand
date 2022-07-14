package com.group4.secondhand.ui.infopenawar

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
class InfoPenawarViewModel @Inject constructor(private val repository: Repository): ViewModel(){
    private var _responseOrder = MutableLiveData<Resource<ResponseSellerOrderById>>()
    val responseOrder : LiveData<Resource<ResponseSellerOrderById>> get() = _responseOrder

    private var _responseStatus = MutableLiveData<Resource<Response<ResponseUpdateStatusProduk>>>()
    val responseStatus : LiveData<Resource<Response<ResponseUpdateStatusProduk>>> get() = _responseStatus

    private var _responseApproveOrder = MutableLiveData<Resource<ResponseApproveOrder>>()
    val responseApproveOrder: LiveData<Resource<ResponseApproveOrder>> get() = _responseApproveOrder

    fun getOrderById(idOrder: Int, token: String){
        viewModelScope.launch {
            _responseOrder.postValue(Resource.loading())
            try {
                _responseOrder.postValue(Resource.success(repository.getSellerOrderById(token, idOrder)))
            } catch (e: Exception){
                _responseOrder.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun updateStatusProduk(token: String, produkId: Int, requestUpdateStatusProduk: RequestUpdateStatusProduk){
        viewModelScope.launch {
            _responseStatus.postValue(Resource.loading())
            try {
                _responseStatus.postValue(Resource.success(repository.updateStatusProduk(token, produkId, requestUpdateStatusProduk)))
            } catch (e: Exception){
                _responseStatus.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun declineOrder(token: String, orderId: Int, requestApproveOrder: RequestApproveOrder){
        viewModelScope.launch {
            _responseApproveOrder.postValue(Resource.loading())
            try {
                _responseApproveOrder.postValue(Resource.success(repository.approveOrder(token, orderId, requestApproveOrder)))
            } catch (e: Exception){
                _responseOrder.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}