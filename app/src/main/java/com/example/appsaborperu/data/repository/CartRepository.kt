package com.example.appsaborperu.data.repository

import com.example.appsaborperu.data.local.dao.CartDao
import com.example.appsaborperu.data.local.entity.CartItemEntity
import com.example.appsaborperu.data.local.entity.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio del Carrito (CRUD).
 * - Observa el carrito del usuario (items + producto).
 * - Agrega, incrementa, decrementa, elimina y vacía.
 * - Calcula el total en CLP.
 */
class CartRepository(
    private val cartDao: CartDao
) {

    /** Observa el carrito del usuario y expone también el total en CLP. */
    fun observeCart(userId: Int): Flow<CartState> =
        cartDao.observeCart(userId).map { items ->
            CartState(
                items = items,
                totalClp = items.sumOf { it.item.quantity * it.item.unitPriceClp }
            )
        }

    /**
     * Agrega un producto al carrito.
     * - Si el item ya existe para (userId, productId), incrementa cantidad.
     * - Si no existe, lo crea con cantidad = 1 y precio unitario (CLP) recibido.
     */
    suspend fun add(userId: Int, productId: Int, unitPriceClp: Int) {
        val existing = cartDao.getItem(userId, productId)
        if (existing == null) {
            cartDao.insert(
                CartItemEntity(
                    userId = userId,
                    productId = productId,
                    quantity = 1,
                    unitPriceClp = unitPriceClp
                )
            )
        } else {
            cartDao.update(existing.copy(quantity = existing.quantity + 1))
        }
    }

    /** Incrementa la cantidad de un ítem específico. */
    suspend fun inc(itemId: Int) {
        val item = cartDao.getById(itemId) ?: return
        cartDao.update(item.copy(quantity = item.quantity + 1))
    }

    /** Decrementa cantidad; si llega a 0, elimina el ítem. */
    suspend fun dec(itemId: Int, currentQuantity: Int? = null) {
        val item = cartDao.getById(itemId) ?: return
        
        // Si se proporciona currentQuantity, usarlo para optimización
        // Si no, obtener la cantidad del item
        val quantity = currentQuantity ?: item.quantity
        
        if (quantity <= 1) {
            cartDao.delete(itemId)
        } else {
            cartDao.update(item.copy(quantity = item.quantity - 1))
        }
    }

    /** Actualiza un ítem completo (cantidad, etc.). Úsalo desde el ViewModel con copy(). */
    suspend fun update(item: CartItemEntity) = cartDao.update(item)

    /** Elimina un ítem por id. */
    suspend fun remove(itemId: Int) = cartDao.delete(itemId)

    /** Vacía el carrito del usuario. */
    suspend fun clear(userId: Int) = cartDao.clearForUser(userId)
}

/** Estado simple del carrito que consume la UI. */
data class CartState(
    val items: List<CartItemWithProduct>,
    val totalClp: Int
)
