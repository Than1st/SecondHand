package com.group4.secondhand.ui.edittawaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseUpdateBuyerOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditTawaranViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _responseUpdate = MutableLiveData<Resource<Response<ResponseUpdateBuyerOrder>>>()
    val responseUpdate: LiveData<Resource<Response<ResponseUpdateBuyerOrder>>> get() = _responseUpdate

    fun updateBuyerOrder(token: String, idOrder: Int, newPrice: Int){
        val priceRequestBody = newPrice.toString().toRequestBody("text/plain".toMediaType())
        viewModelScope.launch {
            _responseUpdate.postValue(Resource.loading())
            try {
                _responseUpdate.postValue(Resource.success(repository.updateBuyerOrder(
                    token,
                    idOrder,
                    priceRequestBody
                )))
            } catch (e: Exception){
                _responseUpdate.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

}