package com.group4.secondhand.ui.notifikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetDataUser
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotifikasiViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private var _notification = MutableLiveData<Resource<List<ResponseNotification>>>()
    val notification : LiveData<Resource<List<ResponseNotification>>> get() = _notification

    private var _user = MutableLiveData<Resource<String>>()
    val user : LiveData<Resource<String>> get() = _user

    fun getToken(){
        viewModelScope.launch {
            _user.postValue(Resource.loading())
            try {
                repository.getToken().collect{
                    _user.postValue(Resource.success(it))
                }
            } catch (e: Exception){
                _user.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun getNotification(token : String){
        viewModelScope.launch {
            _notification.postValue(Resource.loading())
            try {
                _notification.postValue(Resource.success(repository.getNotification(token)))
            } catch (e: Exception){
                _notification.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

}