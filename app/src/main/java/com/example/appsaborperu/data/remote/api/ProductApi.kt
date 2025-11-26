package com.example.appsaborperu.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface de Retrofit para el CRUD de Productos vía API REST.
 */
interface ProductApi {

    // ==================== READ ====================
    
    @GET("products")
    suspend fun getAllProducts(): Response<ApiResponse<List<ProductDto>>>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ApiResponse<ProductDto>>

    // ==================== CREATE ====================
    
    @POST("products")
    suspend fun createProduct(@Body product: ProductRequest): Response<ApiResponse<ProductDto>>

    // ==================== UPDATE ====================
    
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductRequest
    ): Response<ApiResponse<ProductDto>>

    // ==================== DELETE ====================
    
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse<ProductDto>>
}

/**
 * DTO para respuestas de la API.
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

/**
 * DTO del producto que viene de la API.
 * Nota: MySQL devuelve isAvailable como Int (0 o 1), no como Boolean
 */
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val priceClp: Int,
    @SerializedName("isAvailable")
    private val _isAvailable: Any? = null // Puede venir como Int o Boolean
) {
    // Propiedad calculada para manejar ambos tipos (Int y Boolean)
    val isAvailable: Boolean
        get() = when (_isAvailable) {
            is Boolean -> _isAvailable
            is Number -> _isAvailable.toInt() == 1
            else -> true
        }
}

/**
 * DTO para crear/actualizar productos.
 */
data class ProductRequest(
    val name: String,
    val description: String,
    val priceClp: Int,
    val isAvailable: Boolean
)
