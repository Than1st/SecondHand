package com.group4.secondhand.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.RequestBuyerOrder
import com.group4.secondhand.data.model.ResponseBuyerOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private var _token = MutableLiveData<String>()
    val token: MutableLiveData<String> get() = _token

    private var _buyerOrder = MutableLiveData<Resource<ResponseBuyerOrder>>()
    val buyerOrder : LiveData<Resource<ResponseBuyerOrder>> get()= _buyerOrder

    fun getToken() {
        viewModelScope.launch {
            repository.getToken().collect {
                _token.postValue(it)
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
}