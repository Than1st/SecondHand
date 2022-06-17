package com.group4.secondhand.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import com.group4.secondhand.data.model.ResponseLogin
import com.group4.secondhand.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _login = MutableLiveData<Resource<ResponseLogin>>()
    val login : LiveData<Resource<ResponseLogin>> get() = _login

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

    fun setToken(user: User){
        viewModelScope.launch {
            repository.setToken(user)
        }
    }
}