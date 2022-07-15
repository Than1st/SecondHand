package com.group4.secondhand.ui.riwayatpenawaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetBuyerOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatPenawaranViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private var _getRiwayat = MutableLiveData<Resource<List<ResponseGetBuyerOrder>>>()
    val getRiwayat : LiveData<Resource<List<ResponseGetBuyerOrder>>> get() = _getRiwayat

    fun getRiwayatPenawaran(token: String){
        viewModelScope.launch {
            _getRiwayat.postValue(Resource.loading())
            try {
                _getRiwayat.postValue(Resource.success(repository.getBuyerOrder(token)))
            } catch (e: Exception){
                _getRiwayat.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}