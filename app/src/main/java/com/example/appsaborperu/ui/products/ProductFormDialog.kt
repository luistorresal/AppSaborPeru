package com.example.appsaborperu.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.appsaborperu.data.remote.api.ProductDto
import com.example.appsaborperu.ui.theme.PeruRed

/**
 * Diálogo para crear o editar un producto.
 * - Si product es null: modo crear
 * - Si product no es null: modo editar
 * Precios en CLP (pesos chilenos)
 */
@Composable
fun ProductFormDialog(
    product: ProductDto? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String, price: Double, isAvailable: Boolean) -> Unit
) {
    val isEditMode = product != null
    
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var priceText by remember { mutableStateOf(
        if (product != null) product.priceClp.toString() else ""
    ) }
    var isAvailable by remember { mutableStateOf(product?.isAvailable ?: true) }
    
    // Validaciones
    var nameError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            nameError = "El nombre es requerido"
            isValid = false
        } else {
            nameError = null
        }
        
        val price = priceText.toIntOrNull()
        if (price == null || price <= 0) {
            priceError = "Ingrese un precio válido en CLP"
            isValid = false
        } else {
            priceError = null
        }
        
        return isValid
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Título
                Text(
                    text = if (isEditMode) "✏️ Editar Producto" else "➕ Nuevo Producto",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PeruRed
                )
                
                Text(
                    text = "Los cambios se guardarán en la base de datos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Campo Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        nameError = null
                    },
                    label = { Text("Nombre del plato") },
                    placeholder = { Text("Ej: Ceviche Mixto") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PeruRed,
                        focusedLabelColor = PeruRed,
                        cursorColor = PeruRed
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Campo Descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    placeholder = { Text("Describe el plato...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PeruRed,
                        focusedLabelColor = PeruRed,
                        cursorColor = PeruRed
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Campo Precio en CLP
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { 
                        // Solo permitir números
                        if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                            priceText = it
                            priceError = null
                        }
                    },
                    label = { Text("Precio (CLP)") },
                    placeholder = { Text("Ej: 12000") },
                    isError = priceError != null,
                    supportingText = priceError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Text("$") },
                    trailingIcon = { Text("CLP") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PeruRed,
                        focusedLabelColor = PeruRed,
                        cursorColor = PeruRed
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Switch Disponible
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Disponible para venta",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PeruRed,
                            checkedTrackColor = PeruRed.copy(alpha = 0.5f)
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            if (validate()) {
                                val price = priceText.toIntOrNull()?.toDouble() ?: 0.0
                                onSave(name, description, price, isAvailable)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PeruRed)
                    ) {
                        Text(if (isEditMode) "Guardar cambios" else "Crear producto")
                    }
                }
            }
        }
    }
}

/**
 * Diálogo de confirmación para eliminar un producto.
 */
@Composable
fun DeleteConfirmDialog(
    productName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Text("🗑️", style = MaterialTheme.typography.headlineMedium) },
        title = { Text("Eliminar producto") },
        text = { 
            Column {
                Text("¿Estás seguro que deseas eliminar \"$productName\"?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
