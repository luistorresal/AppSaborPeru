package com.example.appsaborperu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsaborperu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO de productos.
 * - getAll(): observar la lista de productos (para la pantalla de catálogo).
 * - insertAll(): insertar la semilla inicial de 4 platos.
 * - count(): saber si ya existe semilla.
 * - getById(): útil para detalles o para agregar al carrito.
 */
@Dao
interface ProductDao {

    /** Observa todos los productos disponibles (orden alfabético). */
    @Query("SELECT * FROM products WHERE isAvailable = 1 ORDER BY name ASC")
    fun getAll(): Flow<List<ProductEntity>>

    /** Inserta varios productos; ignora si el nombre (índice único) ya existe. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductEntity>)

    /** Cantidad de productos en la tabla (para decidir si sembramos datos). */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    /** Obtiene un producto puntual por id. */
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ProductEntity?
}
