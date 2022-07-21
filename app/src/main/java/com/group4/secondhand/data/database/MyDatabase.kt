package com.group4.secondhand.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group4.secondhand.data.model.pagingProduk.Product

@Database(entities = [Product::class,RemoteKeys::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}