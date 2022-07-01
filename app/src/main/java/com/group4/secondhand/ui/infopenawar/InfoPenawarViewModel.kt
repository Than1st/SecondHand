package com.group4.secondhand.ui.infopenawar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetDataUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoPenawarViewModel @Inject constructor(private val repository: Repository): ViewModel(){
    private var _responseOrder = MutableLiveData<Resource<ResponseGetDataUser>>()
    val responseUOrder : LiveData<Resource<ResponseGetDataUser>> get() = _responseOrder

    fun getOrderById(idOrder: Int){
        viewModelScope.launch {
            _responseOrder.postValue(Resource.loading())
            try {
//                _responseOrder.postValue(Resource.success(repository.))
            } catch (e: Exception){
                _responseOrder.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}