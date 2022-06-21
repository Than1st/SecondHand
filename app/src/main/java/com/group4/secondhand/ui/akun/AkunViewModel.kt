package com.group4.secondhand.ui.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AkunViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private var _alreadyLogin = MutableLiveData<String>()
    val alreadyLogin : LiveData<String> get() = _alreadyLogin

    fun getToken(){
        viewModelScope.launch {
            repository.getToken().collect{
                _alreadyLogin.postValue(it)
            }
        }
    }

    fun deleteToken(){
        viewModelScope.launch {
            repository.deleteToken()
        }
    }
}