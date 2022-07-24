package com.group4.secondhand.ui.searchproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.ResponseGetProductSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository):ViewModel(){
    private val _product : MutableLiveData<Resource<List<ResponseGetProductSearch>>> = MutableLiveData()
    val product : LiveData<Resource<List<ResponseGetProductSearch>>> get() = _product

    fun getProduct(status:String,categoryId:String, search: String, page: String, perpage: String){
        viewModelScope.launch {
            _product.postValue(Resource.loading())
            try {
                _product.postValue(Resource.success(repository.getProductSearch(status, categoryId, search, page, perpage)))
            }catch (e:Exception){
                _product.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}