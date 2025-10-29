package com.example.appsaborperu.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa un ítem dentro del carrito de un usuario.
 * - Guardamos el precio en CLP (enteros, sin centavos).
 * - Enforzamos unicidad por (userId, productId) para no duplicar el mismo producto.
 */
@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["productId"]),
        Index(value = ["userId", "productId"], unique = true) // un producto una vez por usuario
    ]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Usuario dueño del carrito */
    val userId: Int,

    /** Producto agregado */
    val productId: Int,

    /** Cantidad agregada (mínimo 1) */
    val quantity: Int,

    /**
     * Precio unitario (snapshot) al momento de agregar al carrito, en CLP.
     * Esto evita inconsistencias si el precio del producto cambia después.
     */
    val unitPriceClp: Int,

    /** Marca de tiempo opcional: millis desde epoch */
    val addedAt: Long = System.currentTimeMillis()
)
