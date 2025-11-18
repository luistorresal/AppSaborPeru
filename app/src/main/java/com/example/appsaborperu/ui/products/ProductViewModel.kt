package com.example.appsaborperu.ui.products

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Product(
    val id: Int,
    val name: String,
    val price: Double
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

class ProductViewModel : ViewModel() {

    // Lista de productos de ejemplo
    val productList = listOf(
        Product(1, "Ceviche", 2500.0),
        Product(2, "Lomo Saltado", 12000.0),
        Product(3, "Ají de Gallina", 11500.0),
        Product(4, "Arroz con Mariscos", 21500.0)
    )

    // Carrito de compras (reactivo)
    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    fun addToCart(product: Product) {
        val index = _cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val item = _cart[index]
            _cart[index] = item.copy(quantity = item.quantity + 1)
        } else {
            _cart.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(product: Product) {
        _cart.removeAll { it.product.id == product.id }
    }

    fun increaseQuantity(product: Product) {
        val index = _cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val item = _cart[index]
            _cart[index] = item.copy(quantity = item.quantity + 1)
        }
    }

    fun decreaseQuantity(product: Product) {
        val index = _cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val item = _cart[index]
            if (item.quantity > 1) {
                _cart[index] = item.copy(quantity = item.quantity - 1)
            } else {
                _cart.removeAt(index)
            }
        }
    }

    fun totalPrice(): Double {
        return _cart.sumOf { it.product.price * it.quantity }
    }

    fun clearCart() {
        _cart.clear()
    }
}
