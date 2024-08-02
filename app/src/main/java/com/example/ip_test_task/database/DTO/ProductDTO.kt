package com.example.ip_test_task.database.DTO

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "Product")
data class ProductDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Int? ,
    val name: String,
    val time: LocalDateTime,
    val tags: List<String>,
    val amount: Int,
)
