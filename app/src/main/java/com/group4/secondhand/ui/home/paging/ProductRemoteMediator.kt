package com.group4.secondhand.ui.home.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.database.MyDatabase
import com.group4.secondhand.data.database.RemoteKeys
import com.group4.secondhand.data.model.pagingProduk.Product

import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val database: MyDatabase,
    private val apiHelper: ApiHelper,
    private val category: Int?=null
) : RemoteMediator<Int, Product>(){

    private val startPage = 1
    override suspend fun initialize()= InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Product>): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH ->{
                val  remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: startPage
            }
            LoadType.PREPEND->{
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND->{
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys!=null)
                nextKey
            }
        }
        try {
            val apiResponse = apiHelper.getProduct(categoryId = category, page = page, perpage = state.config.pageSize)
            val product = apiResponse.body()
            val endOfPagination = product?.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.productDao().clearProducts()
                }
                val prevKey = if (page == startPage) null else page - 1
                val nextKey = if (endOfPagination == true) null else page + 1
                val keys = product?.map {
                    RemoteKeys(productId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                if (keys != null) {
                    database.remoteKeysDao().insertAll(keys)
                }
                if (product != null) {
                    product.forEach {product->
                        val categoryString = product.categories.joinToString { it.name }
                        product.categoryID = categoryString
                    }
                    database.productDao().insertAll(product.sortedBy { it.id })
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination == true)
        }catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Product>): RemoteKeys? {
        return state.pages.lastOrNull(){it.data.isNotEmpty()}?.data?.lastOrNull()?.let { product->
            database.remoteKeysDao().remoteKeysProductId(product.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Product>): RemoteKeys? {
        return state.pages.firstOrNull() {it.data.isNotEmpty()}?.data?.firstOrNull()?.let {product->
            database.remoteKeysDao().remoteKeysProductId(product.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Product>): RemoteKeys? {
        return state.anchorPosition?.let {position->
            state.closestItemToPosition(position)?.id?.let { productId->
                database.remoteKeysDao().remoteKeysProductId(productId)
            }
        }
    }
}