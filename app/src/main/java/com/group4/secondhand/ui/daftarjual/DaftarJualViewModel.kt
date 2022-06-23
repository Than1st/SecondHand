package com.group4.secondhand.ui.daftarjual

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
class DaftarJualViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _user = MutableLiveData<Resource<ResponseGetDataUser>>()
    val user : MutableLiveData<Resource<ResponseGetDataUser>> get() = _user

    private var _token = MutableLiveData<String>()
    val token : MutableLiveData<String> get() = _token

    fun getDataUser(token : String){
        viewModelScope.launch {
            _user.postValue(Resource.loading())
            try {
                _user.postValue(Resource.success(repository.getDataUser(token)))
            } catch (e: Exception){
                _user.postValue((Resource.error(e.localizedMessage?:"Error occured")))
            }
        }
    }

    fun getToken (){
        viewModelScope.launch {
            repository.getToken().collect{
                _token.postValue(it)
            }
        }
    }
}