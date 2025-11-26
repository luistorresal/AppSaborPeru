package com.example.appsaborperu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appsaborperu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO de productos - CRUD completo.
 * - Create: insert(), insertAll()
 * - Read: getAll(), getById()
 * - Update: update()
 * - Delete: delete(), deleteById()
 */
@Dao
interface ProductDao {

    // ==================== CREATE ====================
    
    /** Inserta un nuevo producto y retorna su ID generado. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    /** Inserta varios productos; ignora si el nombre (índice único) ya existe. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductEntity>)

    // ==================== READ ====================
    
    /** Observa todos los productos disponibles (orden alfabético). */
    @Query("SELECT * FROM products WHERE isAvailable = 1 ORDER BY name ASC")
    fun getAll(): Flow<List<ProductEntity>>
    
    /** Observa TODOS los productos incluyendo no disponibles. */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllIncludingUnavailable(): Flow<List<ProductEntity>>

    /** Obtiene un producto puntual por id. */
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ProductEntity?

    /** Cantidad de productos en la tabla (para decidir si sembramos datos). */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    // ==================== UPDATE ====================
    
    /** Actualiza un producto existente. */
    @Update
    suspend fun update(product: ProductEntity)

    // ==================== DELETE ====================
    
    /** Elimina un producto. */
    @Delete
    suspend fun delete(product: ProductEntity)
    
    /** Elimina un producto por su ID. */
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Int)
}
