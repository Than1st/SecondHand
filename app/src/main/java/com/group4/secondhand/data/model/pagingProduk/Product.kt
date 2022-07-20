package com.group4.secondhand.data.model.pagingProduk

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Product")
class Product(
    @PrimaryKey(autoGenerate = true)
    val productId:Int?=null,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_name")
    val imageName: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @ColumnInfo(name = "category_id")
    var categoryID:String?
) {
    @Ignore
    @field:SerializedName("Categories")
    val categories: List<Category> = emptyList()
    data class Category(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}