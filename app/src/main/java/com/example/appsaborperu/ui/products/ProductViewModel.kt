package com.example.appsaborperu.ui.products

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsaborperu.data.remote.api.ProductDto
import com.example.appsaborperu.data.remote.api.ProductRequest
import com.example.appsaborperu.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la gestión de productos - CRUD vía API REST.
 * Conecta la UI con el backend mediante Retrofit.
 * Precios en CLP (pesos chilenos).
 */
class ProductViewModel : ViewModel() {

    private val api = RetrofitClient.productApi

    // Estado de la lista de productos
    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> = _products.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Producto seleccionado para editar
    private val _selectedProduct = MutableStateFlow<ProductDto?>(null)
    val selectedProduct: StateFlow<ProductDto?> = _selectedProduct.asStateFlow()

    // Carrito de compras (reactivo)
    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    init {
        // Cargar productos al iniciar
        loadProducts()
    }

    // ==================== READ ====================

    /** Carga todos los productos desde la API. */
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getAllProducts()
                if (response.isSuccessful && response.body()?.success == true) {
                    _products.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.body()?.message ?: "Error al cargar productos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== CREATE ====================

    /** Crea un nuevo producto vía API. Precio en CLP. */
    fun createProduct(
        name: String,
        description: String,
        price: Double,  // Precio en CLP (ej: 12000)
        isAvailable: Boolean = true
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val priceClp = price.toInt()  // Precio directo en CLP
                val request = ProductRequest(
                    name = name,
                    description = description,
                    priceClp = priceClp,
                    isAvailable = isAvailable
                )
                
                val response = api.createProduct(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    // Recargar la lista para ver el nuevo producto
                    loadProducts()
                } else {
                    _error.value = response.body()?.message ?: "Error al crear producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== UPDATE ====================

    /** Selecciona un producto para editar. */
    fun selectProductForEdit(product: ProductDto) {
        _selectedProduct.value = product
    }

    /** Limpia la selección del producto. */
    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    /** Actualiza un producto existente vía API. */
    fun updateProduct(
        id: Int,
        name: String,
        description: String,
        price: Double,  // Precio en CLP
        isAvailable: Boolean
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val priceClp = price.toInt()  // Precio directo en CLP
                val request = ProductRequest(
                    name = name,
                    description = description,
                    priceClp = priceClp,
                    isAvailable = isAvailable
                )
                
                val response = api.updateProduct(id, request)
                if (response.isSuccessful && response.body()?.success == true) {
                    _selectedProduct.value = null
                    // Recargar la lista para ver los cambios
                    loadProducts()
                } else {
                    _error.value = response.body()?.message ?: "Error al actualizar producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== DELETE ====================

    /** Elimina un producto vía API. */
    fun deleteProduct(product: ProductDto) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.deleteProduct(product.id)
                if (response.isSuccessful && response.body()?.success == true) {
                    // También eliminar del carrito si existe
                    _cart.removeAll { it.productId == product.id }
                    // Recargar la lista
                    loadProducts()
                } else {
                    _error.value = response.body()?.message ?: "Error al eliminar producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== CARRITO ====================

    fun addToCart(product: ProductDto) {
        val index = _cart.indexOfFirst { it.productId == product.id }
        if (index >= 0) {
            val item = _cart[index]
            _cart[index] = item.copy(quantity = item.quantity + 1)
        } else {
            _cart.add(
                CartItem(
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.priceClp.toDouble(),  // Precio en CLP
                    quantity = 1
                )
            )
        }
    }

    fun removeFromCart(productId: Int) {
        _cart.removeAll { it.productId == productId }
    }

    fun increaseQuantity(productId: Int) {
        val index = _cart.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            val item = _cart[index]
            _cart[index] = item.copy(quantity = item.quantity + 1)
        }
    }

    fun decreaseQuantity(productId: Int) {
        val index = _cart.indexOfFirst { it.productId == productId }
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
        return _cart.sumOf { it.productPrice * it.quantity }
    }

    fun clearCart() {
        _cart.clear()
    }

    fun clearError() {
        _error.value = null
    }
}

/**
 * Modelo para items del carrito.
 */
data class CartItem(
    val productId: Int,
    val productName: String,
    val productPrice: Double,  // Precio en CLP
    val quantity: Int
)
