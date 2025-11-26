package com.example.appsaborperu.data.repository

import com.example.appsaborperu.data.local.dao.ProductDao
import com.example.appsaborperu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de Productos - CRUD completo.
 * - Actúa como intermediario entre el ViewModel y el DAO.
 * - Expone operaciones de base de datos de forma limpia.
 */
class ProductRepository(
    private val productDao: ProductDao
) {

    // ==================== CREATE ====================
    
    /** Inserta un nuevo producto y retorna su ID. */
    suspend fun insertProduct(product: ProductEntity): Long {
        return productDao.insert(product)
    }
    
    /** Crea un producto con los datos proporcionados. */
    suspend fun createProduct(
        name: String,
        description: String,
        priceClp: Int,
        isAvailable: Boolean = true
    ): Long {
        val product = ProductEntity(
            name = name,
            description = description,
            priceClp = priceClp,
            isAvailable = isAvailable
        )
        return productDao.insert(product)
    }

    // ==================== READ ====================
    
    /** Observa el catálogo completo (solo disponibles). */
    fun getProducts(): Flow<List<ProductEntity>> = productDao.getAll()
    
    /** Observa todos los productos incluyendo no disponibles. */
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllIncludingUnavailable()

    /** Obtiene un producto puntual (útil para agregar al carrito). */
    suspend fun getById(id: Int): ProductEntity? = productDao.getById(id)

    // ==================== UPDATE ====================
    
    /** Actualiza un producto existente. */
    suspend fun updateProduct(product: ProductEntity) {
        productDao.update(product)
    }
    
    /** Actualiza un producto con los nuevos datos. */
    suspend fun updateProduct(
        id: Int,
        name: String,
        description: String,
        priceClp: Int,
        isAvailable: Boolean
    ) {
        val product = ProductEntity(
            id = id,
            name = name,
            description = description,
            priceClp = priceClp,
            isAvailable = isAvailable
        )
        productDao.update(product)
    }

    // ==================== DELETE ====================
    
    /** Elimina un producto. */
    suspend fun deleteProduct(product: ProductEntity) {
        productDao.delete(product)
    }
    
    /** Elimina un producto por su ID. */
    suspend fun deleteProductById(id: Int) {
        productDao.deleteById(id)
    }

    // ==================== SEED ====================
    
    /** Inserta la semilla inicial SOLO si la tabla está vacía. */
    suspend fun seedIfEmpty() {
        if (productDao.count() == 0) {
            val seed = listOf(
                ProductEntity(
                    name = "Lomo Saltado",
                    description = "Lomo fino salteado con cebolla, tomate y papas fritas.",
                    priceClp = 2800,
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Ají de Gallina",
                    description = "Pollo deshilachado en crema de ají amarillo con arroz.",
                    priceClp = 2400,
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Ceviche Clásico",
                    description = "Pescado fresco marinado en limón con cebolla y camote.",
                    priceClp = 3200,
                    isAvailable = true
                ),
                ProductEntity(
                    name = "Arroz con Mariscos",
                    description = "Arroz con variedad de mariscos al estilo norteño.",
                    priceClp = 3500,
                    isAvailable = true
                )
            )
            productDao.insertAll(seed)
        }
    }
}
