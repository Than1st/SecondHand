package com.group4.secondhand.ui.editproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseDeleteSellerProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _token = MutableLiveData<String>()
    val token: MutableLiveData<String> get() = _token

    private var _delete : MutableLiveData<Resource<ResponseDeleteSellerProduct>> = MutableLiveData()
    val delete : LiveData<Resource<ResponseDeleteSellerProduct>> get() = _delete


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