package com.example.ip_test_task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ip_test_task.database.DAO.ProductDao
import com.example.ip_test_task.database.DTO.ProductDTO
import com.example.ip_test_task.database.helpers.converters.ListToStringConverter
import com.example.ip_test_task.database.helpers.converters.LocalDateTimeConverter

@Database(
    entities = [ProductDTO::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeConverter::class,
    ListToStringConverter::class
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDao: ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDbInstance(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "ProductDatabase"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = inst
                inst
            }

        }
    }
}