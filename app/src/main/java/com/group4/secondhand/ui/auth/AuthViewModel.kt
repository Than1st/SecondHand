package com.group4.secondhand.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _login = MutableLiveData<Resource<ResponseLogin>>()
    val login : LiveData<Resource<ResponseLogin>> get() = _login

    private val _register : MutableLiveData<Resource<ResponseRegister>> = MutableLiveData()
    val register: LiveData<Resource<ResponseRegister>> get() = _register

    fun authLogin(requestLogin: RequestLogin){
        viewModelScope.launch {
            _login.postValue(Resource.loading())
            try {
                _login.postValue(Resource.success(repository.authLogin(requestLogin)))
            } catch (e: Exception){
                _login.postValue(Resource.error(e.message ?: "Error"))
            }
        }
    }

    fun authRegister(requestRegister: RequestRegister){
        viewModelScope.launch {
            _register.postValue(Resource.loading())
            try {
                _register.postValue(Resource.success(repository.authRegister(requestRegister)))
            } catch (e: Exception) {
                _register.postValue(Resource.error(e.message ?: "Error"))
            }
        }
    }

    fun setToken(token: String){
        viewModelScope.launch {
            repository.setToken(token)
        }
    }
}