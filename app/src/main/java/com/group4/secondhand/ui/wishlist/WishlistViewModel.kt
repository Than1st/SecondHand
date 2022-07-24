package com.group4.secondhand.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetBuyerOrder
import com.group4.secondhand.data.model.ResponseGetBuyerWishlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _token = MutableLiveData<Resource<String>>()
    val token : LiveData<Resource<String>> get() = _token

    private val _getBuyerWishlist : MutableLiveData<Resource<List<ResponseGetBuyerWishlist>>> = MutableLiveData()
    val getBuyerWishlist : LiveData<Resource<List<ResponseGetBuyerWishlist>>> get() = _getBuyerWishlist

    fun getToken(){
        viewModelScope.launch {
            _token.postValue(Resource.loading())
            try {
                repository.getToken().collect{
                    _token.postValue(Resource.success(it))
                }
            } catch (e: Exception){
                _token.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun getBuyerWishlist(token: String) {
        viewModelScope.launch {
            _getBuyerWishlist.postValue(Resource.loading())
            try {
                _getBuyerWishlist.postValue(Resource.success(repository.getBuyerWishlist(token)))
            } catch (e: Exception) {
                _getBuyerWishlist.postValue(Resource.error(e.localizedMessage ?: "Error occurred"))
            }
        }
    }
}