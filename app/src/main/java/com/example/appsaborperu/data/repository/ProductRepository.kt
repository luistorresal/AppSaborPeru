package com.example.appsaborperu.data.repository

import com.example.appsaborperu.data.local.dao.ProductDao
import com.example.appsaborperu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de Productos.
 * - Expone el catálogo como Flow para la pantalla de Productos.
 * - Inserta 4 platos de SaborPeru si la tabla está vacía (seed).
 */
class ProductRepository(
    private val productDao: ProductDao
) {

    /** Observa el catálogo completo (solo disponibles). */
    fun getProducts(): Flow<List<ProductEntity>> = productDao.getAll()

    /** Obtiene un producto puntual (útil para agregar al carrito). */
    suspend fun getById(id: Int): ProductEntity? = productDao.getById(id)

    /** Inserta la semilla inicial SOLO si la tabla está vacía. */
    suspend fun seedIfEmpty() {
        if (productDao.count() == 0) {
            val seed = listOf(
                ProductEntity(
                    name = "Lomo Saltado",
                    description = "Lomo fino salteado con cebolla, tomate y papas.",
                    priceClp = 28000, // CLP (ej: 28.000)
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Ají de Gallina",
                    description = "Pollo deshilachado en crema de ají amarillo.",
                    priceClp = 24000,
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Tallarín Saltado",
                    description = "Salteado al wok con verduras y salsa oriental.",
                    priceClp = 26000,
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Causa Vegana Limeña",
                    description = "Capa de papa amarilla con relleno vegetal.",
                    priceClp = 22000,
                    isAvailable = true
                )
            )
            productDao.insertAll(seed)
        }
    }
}
