package com.example.ta_mobile.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("cart_product_entity", indices = [Index(value = ["product_id"], unique = true)])
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "store_name")
    val storeName: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "amount_purchase")
    val amountPurchase: Int

)