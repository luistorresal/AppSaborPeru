package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val priceClp: Int,
    val isAvailable: Boolean = true
)

@Serializable
data class ProductRequest(
    val name: String,
    val description: String = "",
    val priceClp: Int,
    val isAvailable: Boolean = true
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

