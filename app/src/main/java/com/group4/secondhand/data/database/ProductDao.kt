package com.group4.secondhand.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.group4.secondhand.data.model.pagingProduk.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products:List<Product>)
    @Query("SELECT * FROM product ORDER BY productId ASC")
    fun getProduct(): PagingSource<Int, Product>
    @Query("DELETE FROM product")
    suspend fun clearProducts()
}