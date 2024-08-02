package com.example.ip_test_task.database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.ip_test_task.database.DTO.ProductDTO

@Dao
interface ProductDao {

    @Upsert
    suspend fun upsertProduct(product: ProductDTO)

    @Query("SELECT * FROM Product")
    fun selectAllProducts(): List<ProductDTO>

    @Query("SELECT * FROM Product LIMIT :limit")
    fun selectPaginatedProducts(limit: Int): List<ProductDTO>

    @Delete
    suspend fun deleteProduct(product: ProductDTO)
}