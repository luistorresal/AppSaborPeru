package com.example.appsaborperu.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo de productos") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Platos disponibles 🍽️",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.productList) { product ->
                    ProductItem(
                        product = product,
                        onAddToCart = { viewModel.addToCart(product) }
                    )
                }
            }

            Divider()

            Text(
                text = "🛒 Carrito de compras",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                items(viewModel.cart) { item ->
                    CartItemView(
                        item = item,
                        onIncrease = { viewModel.increaseQuantity(item.product) },
                        onDecrease = { viewModel.decreaseQuantity(item.product) },
                        onRemove = { viewModel.removeFromCart(item.product) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: S/. ${String.format("%.2f", viewModel.totalPrice())}",
                style = MaterialTheme.typography.titleLarge
            )

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
}

@Composable
fun ProductItem(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(product.name, style = MaterialTheme.typography.bodyLarge)
                Text("$ . ${product.price}", style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = onAddToCart) {
                Text("Agregar")
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
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(item.product.name, style = MaterialTheme.typography.bodyLarge)
                Text("$ . ${item.product.price * item.quantity}", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                Button(onClick = onDecrease) { Text("-") }
                Text("${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
                Button(onClick = onIncrease) { Text("+") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onRemove) { Text("🗑️") }
            }
        }
    }
}
