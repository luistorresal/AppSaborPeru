package com.example.appsaborperu.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsaborperu.data.remote.api.ProductDto
import com.example.appsaborperu.ui.theme.PeruRed
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    userName: String = "",
    userEmail: String = "",
    userRole: String = "",
    onLogout: () -> Unit = {},
    viewModel: ProductViewModel = viewModel()
) {
    // Determinar permisos según el rol
    val isAdmin = userRole == "admin"  // marines y claudio
    val isCliente = userRole == "cliente"  // luis
    
    // Estados del ViewModel
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    // Estados locales para diálogos
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductDto?>(null) }
    var showPurchaseSuccess by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    // Formateador de precios CLP
    val clpFormat = remember {
        NumberFormat.getNumberInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "🍽️ Sabor Perú",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Hola, $userName",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PeruRed,
                    titleContentColor = Color.White
                ),
                actions = {
                    // Botón de refrescar
                    IconButton(onClick = { viewModel.loadProducts() }) {
                        Icon(
                            Icons.Default.Refresh, 
                            contentDescription = "Refrescar",
                            tint = Color.White
                        )
                    }
                    // Botón de cerrar sesión
                    IconButton(onClick = { showLogoutConfirm = true }) {
                        Icon(
                            Icons.Default.ExitToApp, 
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // Solo admins pueden agregar productos
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = PeruRed
                ) {
                    Icon(
                        Icons.Default.Add, 
                        contentDescription = "Agregar producto",
                        tint = Color.White
                    )
                }
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

            // Indicador de rol del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAdmin) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAdmin) "👨‍💼 Administrador" else "🛒 Cliente",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAdmin) "• Puede editar y eliminar productos" else "• Puede realizar compras",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            // Título de sección
            Text(
                text = "Catálogo de Platos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "${products.size} productos disponibles",
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
                        CircularProgressIndicator(color = PeruRed)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando productos...")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductItem(
                            product = product,
                            clpFormat = clpFormat,
                            isAdmin = isAdmin,
                            isCliente = isCliente,
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

            // ==================== CARRITO (SOLO PARA CLIENTE - LUIS) ====================
            if (isCliente) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🛒 Mi Carrito",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (viewModel.cart.isNotEmpty()) {
                        Text(
                            text = "${viewModel.cart.sumOf { it.quantity }} items",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                if (viewModel.cart.isEmpty()) {
                    Text(
                        text = "Tu carrito está vacío. ¡Agrega productos!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(top = 8.dp)
                    ) {
                        items(viewModel.cart) { item ->
                            CartItemView(
                                item = item,
                                clpFormat = clpFormat,
                                onIncrease = { viewModel.increaseQuantity(item.productId) },
                                onDecrease = { viewModel.decreaseQuantity(item.productId) },
                                onRemove = { viewModel.removeFromCart(item.productId) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Total y botón de comprar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = PeruRed.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$ ${clpFormat.format(viewModel.totalPrice().toInt())} CLP",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PeruRed
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Botón de COMPRAR
                        Button(
                            onClick = { 
                                showPurchaseSuccess = true
                                viewModel.clearCart()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = viewModel.cart.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PeruRed
                            )
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Realizar Compra",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // ==================== DIÁLOGOS ====================

    // Diálogo para CREAR producto (solo admins)
    if (showCreateDialog && isAdmin) {
        ProductFormDialog(
            product = null,
            onDismiss = { showCreateDialog = false },
            onSave = { name, description, price, isAvailable ->
                viewModel.createProduct(name, description, price, isAvailable)
                showCreateDialog = false
            }
        )
    }

    // Diálogo para EDITAR producto (solo admins)
    if (showEditDialog && selectedProduct != null && isAdmin) {
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

    // Diálogo para ELIMINAR producto (solo admins)
    if (productToDelete != null && isAdmin) {
        DeleteConfirmDialog(
            productName = productToDelete!!.name,
            onDismiss = { productToDelete = null },
            onConfirm = {
                viewModel.deleteProduct(productToDelete!!)
                productToDelete = null
            }
        )
    }

    // Diálogo de confirmación de COMPRA (solo cliente)
    if (showPurchaseSuccess) {
        AlertDialog(
            onDismissRequest = { showPurchaseSuccess = false },
            icon = { Text("✅", style = MaterialTheme.typography.headlineLarge) },
            title = { Text("¡Compra Exitosa!") },
            text = { 
                Column {
                    Text("Tu pedido ha sido procesado correctamente.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Gracias por tu compra, $userName.",
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPurchaseSuccess = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PeruRed)
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de confirmación de CERRAR SESIÓN
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            icon = { 
                Icon(
                    Icons.Default.ExitToApp, 
                    contentDescription = null,
                    tint = PeruRed
                ) 
            },
            title = { Text("Cerrar Sesión") },
            text = { 
                Text("¿Estás seguro que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = { 
                        showLogoutConfirm = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PeruRed)
                ) {
                    Text("Sí, cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProductItem(
    product: ProductDto,
    clpFormat: NumberFormat,
    isAdmin: Boolean,
    isCliente: Boolean,
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
                        text = "$ ${clpFormat.format(product.priceClp)} CLP",
                        style = MaterialTheme.typography.titleMedium,
                        color = PeruRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botones de acción según el rol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botones de CRUD (solo para admins)
                if (isAdmin) {
                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = PeruRed
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
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
                // Botón agregar al carrito (solo para cliente - Luis)
                if (isCliente) {
                    Button(
                        onClick = onAddToCart,
                        colors = ButtonDefaults.buttonColors(containerColor = PeruRed)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemView(
    item: CartItem,
    clpFormat: NumberFormat,
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
                    text = "$ ${clpFormat.format((item.productPrice * item.quantity).toInt())} CLP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PeruRed
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
