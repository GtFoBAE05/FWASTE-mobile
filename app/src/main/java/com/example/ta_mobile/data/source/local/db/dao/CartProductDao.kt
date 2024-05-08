package com.example.ta_mobile.data.source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {

    @Query("SELECT * FROM cart_product_entity ORDER BY id")
    fun getCartProduct(): Flow<List<CartProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: CartProductEntity)

    @Query("UPDATE cart_product_entity SET amount_purchase =:amount WHERE product_id=:productId")
    suspend fun updateCartProductAmount(productId: String, amount:Int)

    @Query("DELETE FROM cart_product_entity WHERE product_id =:productId")
    suspend fun deleteByProductId(productId: String)

    @Query("DELETE FROM cart_product_entity")
    suspend fun deleteAll()

}