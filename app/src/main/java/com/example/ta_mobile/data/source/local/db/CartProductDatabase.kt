package com.example.ta_mobile.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ta_mobile.data.source.local.db.dao.CartProductDao
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity

@Database(
    entities =[CartProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CartProductDatabase : RoomDatabase(){
    abstract fun dao():CartProductDao
}