package com.group4.secondhand.ui.editakun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.model.RequestUpdateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAkunViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    fun updateDataUser(token : String, requestUpdateUser: RequestUpdateUser){
        viewModelScope.launch {
            repository.updateDataUser(token, requestUpdateUser)
        }
    }
}