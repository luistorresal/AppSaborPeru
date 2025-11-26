package com.example.appsaborperu.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsaborperu.data.remote.api.ProductDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {
    
    // Estados del ViewModel
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    // Estados locales para diálogos
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductDto?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "🍽️ Sabor Perú - API",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    // Botón de refrescar
                    IconButton(onClick = { viewModel.loadProducts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Mostrar error si existe
            error?.let { errorMessage ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Indicador de conexión API
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "🌐 Conectado a API REST (MySQL)",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Título de sección
            Text(
                text = "Catálogo de Platos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "${products.size} productos en la base de datos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de productos o loading
            if (isLoading && products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando desde API...")
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(products, key = { it.id }) { product ->
                        ProductItem(
                            product = product,
                            onAddToCart = { viewModel.addToCart(product) },
                            onEdit = { 
                                viewModel.selectProductForEdit(product)
                                showEditDialog = true
                            },
                            onDelete = { productToDelete = product }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Sección Carrito
            Text(
                text = "🛒 Carrito de Compras",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (viewModel.cart.isEmpty()) {
                Text(
                    text = "El carrito está vacío",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(top = 8.dp)
                ) {
                    items(viewModel.cart) { item ->
                        CartItemView(
                            item = item,
                            onIncrease = { viewModel.increaseQuantity(item.productId) },
                            onDecrease = { viewModel.decreaseQuantity(item.productId) },
                            onRemove = { viewModel.removeFromCart(item.productId) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "S/. ${String.format("%.2f", viewModel.totalPrice())}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.clearCart() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.cart.isNotEmpty()
            ) {
                Text("Vaciar carrito")
            }
        }
    }

    // ==================== DIÁLOGOS ====================

    // Diálogo para CREAR producto
    if (showCreateDialog) {
        ProductFormDialog(
            product = null,
            onDismiss = { showCreateDialog = false },
            onSave = { name, description, price, isAvailable ->
                viewModel.createProduct(name, description, price, isAvailable)
                showCreateDialog = false
            }
        )
    }

    // Diálogo para EDITAR producto
    if (showEditDialog && selectedProduct != null) {
        ProductFormDialog(
            product = selectedProduct,
            onDismiss = { 
                showEditDialog = false
                viewModel.clearSelectedProduct()
            },
            onSave = { name, description, price, isAvailable ->
                selectedProduct?.let { product ->
                    viewModel.updateProduct(
                        id = product.id,
                        name = name,
                        description = description,
                        price = price,
                        isAvailable = isAvailable
                    )
                }
                showEditDialog = false
            }
        )
    }

    // Diálogo para ELIMINAR producto
    productToDelete?.let { product ->
        DeleteConfirmDialog(
            productName = product.name,
            onDismiss = { productToDelete = null },
            onConfirm = {
                viewModel.deleteProduct(product)
                productToDelete = null
            }
        )
    }
}

@Composable
fun ProductItem(
    product: ProductDto,
    onAddToCart: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (!product.description.isNullOrBlank()) {
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "S/. ${String.format("%.2f", product.priceClp / 100.0)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botones de CRUD (Editar y Eliminar)
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                // Botón agregar al carrito
                Button(onClick = onAddToCart) {
                    Text("Agregar")
                }
            }
        }
    }
}

@Composable
fun CartItemView(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "S/. ${String.format("%.2f", item.productPrice * item.quantity)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(36.dp)
                ) { 
                    Text("-", fontWeight = FontWeight.Bold) 
                }
                
                Text(
                    text = "${item.quantity}",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                FilledTonalIconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(36.dp)
                ) { 
                    Text("+", fontWeight = FontWeight.Bold) 
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar del carrito",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
