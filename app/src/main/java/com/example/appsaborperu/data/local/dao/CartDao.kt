package com.example.appsaborperu.data.local.dao

import androidx.room.*
import com.example.appsaborperu.data.local.entity.CartItemEntity
import com.example.appsaborperu.data.local.entity.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

/**
 * DAO para manejar las operaciones del carrito de compras.
 * Incluye:
 * - Agregar productos
 * - Modificar cantidad
 * - Eliminar ítem o vaciar carrito
 * - Observar el carrito completo con detalles de producto
 */
@Dao
interface CartDao {

    /** Observa los productos del carrito de un usuario */
    @Transaction
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun observeCart(userId: Int): Flow<List<CartItemWithProduct>>

    /** Obtiene un ítem específico del carrito (por usuario y producto) */
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getItem(userId: Int, productId: Int): CartItemEntity?

    /** Obtiene un ítem del carrito por su ID */
    @Query("SELECT * FROM cart_items WHERE id = :itemId LIMIT 1")
    suspend fun getById(itemId: Int): CartItemEntity?

    /** Inserta un ítem (nuevo producto en el carrito) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)

    /** Actualiza un ítem existente (por ejemplo, cambiar cantidad) */
    @Update
    suspend fun update(item: CartItemEntity)

    /** Elimina un ítem del carrito */
    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun delete(itemId: Int)

    /** Vacía el carrito completo de un usuario */
    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearForUser(userId: Int)
}
