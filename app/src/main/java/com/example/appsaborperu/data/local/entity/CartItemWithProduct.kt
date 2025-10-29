package com.example.appsaborperu.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * POJO de relación para leer el carrito junto con los datos del producto.
 * Útil para la pantalla del carrito (nombre, precio, etc.) sin hacer joins manuales.
 */
data class CartItemWithProduct(
    @Embedded
    val item: CartItemEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)
