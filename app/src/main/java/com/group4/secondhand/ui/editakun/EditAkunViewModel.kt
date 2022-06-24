package com.group4.secondhand.ui.editakun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditAkunViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    fun updateDataUser(
        token : String,
        file: File?,
        name: String,
        phoneNumber: String,
        address: String,
        city: String,
    ){
        val requestFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image = requestFile?.let { MultipartBody.Part.createFormData("image", file.name, it) }
        val namaRequestBody = name.toRequestBody("text/plain".toMediaType())
        val kotaRequestBody = city.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = address.toRequestBody("text/plain".toMediaType())
        val noHpRequestBody = phoneNumber.toRequestBody("text/plain".toMediaType())
        viewModelScope.launch {
            repository.updateDataUser(token, image, namaRequestBody, noHpRequestBody,alamatRequestBody, kotaRequestBody)
        }
    }
}