package com.group4.secondhand.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys:List<RemoteKeys>)
    @Query("SELECT * FROM remote_keys WHERE productId = :productId")
    suspend fun remoteKeysProductId(productId:Int):RemoteKeys?
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}