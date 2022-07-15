package com.group4.secondhand.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.data.model.ResponseGetBanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private val _category = MutableLiveData<Resource<List<ResponseCategoryHome>>>()
    val category : LiveData<Resource<List<ResponseCategoryHome>>> get() = _category

    private val _product : MutableLiveData<Resource<List<ResponseGetProduct>>> = MutableLiveData()
    val product : LiveData<Resource<List<ResponseGetProduct>>> get() = _product

    private val _banner : MutableLiveData<Resource<List<ResponseGetBanner>>> = MutableLiveData()
    val banner : LiveData<Resource<List<ResponseGetBanner>>> get() = _banner

    fun getBannerHome(){
        viewModelScope.launch {
            _banner.postValue(Resource.loading())
            try {
                _banner.postValue(Resource.success(repository.getBanner()))
            }catch (e:Exception){
                _banner.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }


    fun getCategoryHome(){
        viewModelScope.launch {
            _category.postValue(Resource.loading())
            try {
                _category.postValue(Resource.success(repository.getCategoryHome()))
            }catch (e:Exception){
                _category.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }

    fun getProduct(status:String,categoryId:String, search: String, page: String, perpage: String){
        viewModelScope.launch {
            _product.postValue(Resource.loading())
            try {
                _product.postValue(Resource.success(repository.getProduct(status, categoryId, search, page, perpage)))
            }catch (e:Exception){
                _product.postValue(Resource.error(e.localizedMessage?:"Error occured"))
            }
        }
    }
}