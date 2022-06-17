package com.group4.secondhand.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.Resource
import com.group4.secondhand.data.model.ResponseCategoryHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private val _category = MutableLiveData<Resource<List<ResponseCategoryHome>>>()
    val category : LiveData<Resource<List<ResponseCategoryHome>>> get() = _category

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
}