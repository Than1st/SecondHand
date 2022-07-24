package com.group4.secondhand.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.database.DbHelper
import com.group4.secondhand.data.database.MyDatabase
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.RequestApproveOrder
import com.group4.secondhand.data.model.RequestBuyerOrder
import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.data.model.RequestRegister
import com.group4.secondhand.data.model.pagingProduk.Product
import com.group4.secondhand.ui.home.paging.ProductRemoteMediator
import kotlinx.coroutines.flow.Flow
import com.group4.secondhand.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(private val apiHelper: ApiHelper, private val userPreferences: UserPreferences,  private val database: MyDatabase) {
    companion object {
        const val PAGE_SIZE = 30
    }
    // SELLER
    suspend fun getBanner() = apiHelper.getBanner()
    suspend fun getCategoryHome() = apiHelper.getCategoryHome()
    suspend fun getSellerProduct(token: String) = apiHelper.getSellerProduct(token)
    suspend fun getSellerOrder(token: String,status: String) = apiHelper.getSellerOrder(token,status)
    suspend fun getSellerOrderById(token: String,orderId: Int) = apiHelper.getSellerOrderById(token,orderId)
    suspend fun deleteSellerProduct(token: String,id: Int) = apiHelper.deleteSellerProduct(token,id)
    suspend fun approveOrder(token: String, id: Int, requestApproveOrder: RequestApproveOrder) = apiHelper.approveOrder(token, id, requestApproveOrder)
    suspend fun updateStatusProduk(token: String,produkId: Int, requestUpdateStatusProduk: RequestUpdateStatusProduk) = apiHelper.updateStatusProduk(token, produkId, requestUpdateStatusProduk)
    suspend fun uploadProduct(
        token: String,
        file : MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiHelper.uploadProduct(token, file, name, description, base_price, categoryIds, location)

    suspend fun updateProduct(
        token: String,
        id: Int,
        file : MultipartBody.Part?,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        categoryIds: List<Int>,
        location: RequestBody,
    ) = apiHelper.updateProduct(token,id, file, name, description, base_price, categoryIds, location)

    // BUYER
    suspend fun getProductSearch(status: String, categoryId: String, search: String, page: String, perpage: String) =
        apiHelper.getProductSearch(status, categoryId,search, page, perpage)

    suspend fun getProductById (id : Int) = apiHelper.getProductById(id)

    suspend fun buyerOrder(token: String,requestBuyerOrder: RequestBuyerOrder) = apiHelper.buyerOrder(token, requestBuyerOrder)

    suspend fun getBuyerWishlist(token: String) = apiHelper.getBuyerWishlist(token)

    suspend fun getBuyerOrder(token: String) = apiHelper.getBuyerOrder(token)

    suspend fun getBuyerOrderById(token: String, id: Int) = apiHelper.getBuyerOrderById(token, id)

    suspend fun updateBuyerOrder(token: String, id: Int, newPrice: RequestBody) = apiHelper.updateBuyerOrder(token, id, newPrice)

    suspend fun deleteBuyerOrder(token: String, id: Int) = apiHelper.deleteBuyerOrder(token, id)

    suspend fun addWishlist(token: String, productId: RequestBody) = apiHelper.addWishlist(token, productId)

    suspend fun removeWishlist(token: String,id: Int) = apiHelper.removeWishlist(token,id)

    // AUTH
    suspend fun authRegister(requestRegister: RequestRegister) =
        apiHelper.authRegister(requestRegister)

    suspend fun authLogin(requestLogin: RequestLogin) = apiHelper.authLogin(requestLogin)
    suspend fun getDataUser(token: String) = apiHelper.getDataUser(token)
    suspend fun changePassword(token: String, currentPassword: RequestBody, newPassword: RequestBody, confPassword: RequestBody) = apiHelper.changePassword(token, currentPassword, newPassword, confPassword)
    suspend fun updateDataUser(
        token : String,
        image: MultipartBody.Part?,
        name: RequestBody?,
        phoneNumber: RequestBody?,
        address: RequestBody?,
        city: RequestBody?,
    ) = apiHelper.updateDataUser(token, image, name, phoneNumber, address, city)

    // NOTIFICATION
    suspend fun getNotification(token: String) = apiHelper.getNotification(token)
    suspend fun markReadNotification(token: String, id: Int) = apiHelper.markReadNotification(token, id)

    // DATA STORE
    suspend fun setToken(token: String) = userPreferences.setToken(token)
    fun getToken() = userPreferences.getToken()
    suspend fun deleteToken() = userPreferences.deleteToken()

    //fetching product
    fun getProductStream(categoryId: Int? = null): Flow<PagingData<Product>> {

        val pagingSourceFactory = { database.productDao().getProduct() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ProductRemoteMediator(database, apiHelper, categoryId),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }
}