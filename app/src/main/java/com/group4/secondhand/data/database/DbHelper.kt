package com.group4.secondhand.data.database

import com.group4.secondhand.data.model.pagingProduk.Product

class DbHelper(
    private val productDao: ProductDao,
    private val remoteKeysDao: RemoteKeysDao ) {

}
