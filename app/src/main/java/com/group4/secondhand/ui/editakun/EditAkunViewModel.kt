package com.group4.secondhand.ui.editakun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.model.RequestUpdateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class EditAkunViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    fun updateDataUser(token : String, image: MultipartBody.Part?, name: RequestBody?){
        viewModelScope.launch {
            repository.updateDataUser(token, image, name)
        }
    }
}